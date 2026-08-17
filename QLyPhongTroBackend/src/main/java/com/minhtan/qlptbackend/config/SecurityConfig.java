package com.minhtan.qlptbackend.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private static final int MAX_REQUESTS_PER_WINDOW = 120;
    private static final Duration WINDOW_DURATION = Duration.ofSeconds(60);

    // Xóa các IP không hoạt động trong khoảng thời gian này
    private static final Duration BUCKET_EXPIRATION = Duration.ofMinutes(10);

    private static final Map<String, WindowState> REQUEST_BUCKETS =
            new ConcurrentHashMap<>();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .anyRequest().permitAll()
            )

            .headers(headers -> headers
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .preload(true)
                    .maxAgeInSeconds(31536000)
                )
            )

            .exceptionHandling(exception ->
                exception.accessDeniedHandler(new AccessDeniedHandlerImpl())
            );

        http.addFilterBefore(
            rateLimitFilter(),
            SecurityContextHolderFilter.class
        );

        return http.build();
    }

    @Bean
    public OncePerRequestFilter rateLimitFilter() {

        return new OncePerRequestFilter() {

            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                String path = request.getRequestURI();

                return path.startsWith("/actuator")
                        || "OPTIONS".equalsIgnoreCase(request.getMethod());
            }

            @Override
            protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain
            ) throws ServletException {

                try {
                    String clientIp = resolveClientIp(request);

                    WindowState state = REQUEST_BUCKETS.computeIfAbsent(
                        clientIp,
                        key -> new WindowState()
                    );

                    long now = System.currentTimeMillis();

                    synchronized (state) {

                        // Window đã hết hạn → reset
                        if (now - state.windowStartMs >= WINDOW_DURATION.toMillis()) {
                            state.windowStartMs = now;
                            state.count = 0;
                        }

                        // IP đã vượt giới hạn
                        if (state.count >= MAX_REQUESTS_PER_WINDOW) {

                            long elapsed =
                                    now - state.windowStartMs;

                            long remainingMs =
                                    WINDOW_DURATION.toMillis() - elapsed;

                            long retryAfterSeconds =
                                    Math.max(
                                        1,
                                        (remainingMs + 999) / 1000
                                    );

                            response.setStatus(
                                HttpStatus.TOO_MANY_REQUESTS.value()
                            );

                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            response.setHeader(
                                "Retry-After",
                                String.valueOf(retryAfterSeconds)
                            );

                            response.setHeader(
                                "X-RateLimit-Limit",
                                String.valueOf(MAX_REQUESTS_PER_WINDOW)
                            );

                            response.setHeader(
                                "X-RateLimit-Remaining",
                                "0"
                            );

                            response.getWriter().write(
                                "{\"message\":\"Too many requests. Please slow down.\"}"
                            );

                            return;
                        }

                        state.count++;
                        state.lastAccessMs = now;

                        response.setHeader(
                            "X-RateLimit-Limit",
                            String.valueOf(MAX_REQUESTS_PER_WINDOW)
                        );

                        response.setHeader(
                            "X-RateLimit-Remaining",
                            String.valueOf(
                                Math.max(
                                    0,
                                    MAX_REQUESTS_PER_WINDOW - state.count
                                )
                            )
                        );
                    }

                    filterChain.doFilter(request, response);

                } catch (Exception ex) {

                    log.error(
                        "Error while processing rate limit filter",
                        ex
                    );

                    if (!response.isCommitted()) {
                        response.setStatus(
                            HttpStatus.INTERNAL_SERVER_ERROR.value()
                        );
                    }
                }
            }

            private String resolveClientIp(HttpServletRequest request) {

                /*
                 * Nếu Spring Boot đứng sau Nginx/Cloudflare,
                 * nên cấu hình proxy để xác định IP thật.
                 *
                 * Không nên tin X-Forwarded-For từ client trực tiếp.
                 *
                 * Vì vậy ở tầng Java, mặc định sử dụng
                 * remote address.
                 */
                return request.getRemoteAddr();
            }
        };
    }

    /**
     * Xóa các IP không còn hoạt động để tránh REQUEST_BUCKETS
     * tăng vô hạn và gây tốn RAM.
     */
    @Bean
    public Thread rateLimitCleanupThread() {

        Thread cleanupThread = new Thread(() -> {

            while (!Thread.currentThread().isInterrupted()) {

                try {

                    Thread.sleep(
                        Duration.ofMinutes(5).toMillis()
                    );

                    long now = System.currentTimeMillis();

                    REQUEST_BUCKETS.entrySet().removeIf(entry -> {

                        WindowState state = entry.getValue();

                        return now - state.lastAccessMs
                                >= BUCKET_EXPIRATION.toMillis();
                    });

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                    log.info(
                        "Rate limit cleanup thread stopped."
                    );

                    break;
                } catch (Exception e) {

                    log.error(
                        "Error during rate limit bucket cleanup",
                        e
                    );
                }
            }

        }, "rate-limit-cleanup");

        cleanupThread.setDaemon(true);
        cleanupThread.start();

        return cleanupThread;
    }

    private static final class WindowState {

        private long windowStartMs =
                System.currentTimeMillis();

        private long lastAccessMs =
                System.currentTimeMillis();

        private int count = 0;
    }
}