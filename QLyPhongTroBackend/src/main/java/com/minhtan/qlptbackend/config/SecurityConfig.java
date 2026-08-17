package com.minhtan.qlptbackend.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final int MAX_REQUESTS_PER_WINDOW = 120;
    private static final Duration WINDOW_DURATION = Duration.ofSeconds(60);
    private static final Map<String, WindowState> REQUEST_BUCKETS = new ConcurrentHashMap<>();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .anyRequest().permitAll())
            .headers(headers -> headers
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .preload(true)
                    .maxAgeInSeconds(31536000)))
            .exceptionHandling(exception -> exception
                .accessDeniedHandler(new AccessDeniedHandlerImpl()));

        http.addFilterBefore(rateLimitFilter(), SecurityContextHolderFilter.class);

        return http.build();
    }

    @Bean
    public OncePerRequestFilter rateLimitFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                String path = request.getRequestURI();
                return path.startsWith("/actuator") || "OPTIONS".equalsIgnoreCase(request.getMethod());
            }

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
                try {
                    String clientIp = resolveClientIp(request);
                    WindowState state = REQUEST_BUCKETS.computeIfAbsent(clientIp, key -> new WindowState());

                    synchronized (state) {
                        long now = System.currentTimeMillis();
                        if (now - state.windowStartMs >= WINDOW_DURATION.toMillis()) {
                            state.windowStartMs = now;
                            state.count = 0;
                        }

                        if (state.count >= MAX_REQUESTS_PER_WINDOW) {
                            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.setHeader("Retry-After", String.valueOf(WINDOW_DURATION.getSeconds()));
                            response.setHeader("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS_PER_WINDOW));
                            response.getWriter().write("{\"message\":\"Too many requests. Please slow down.\"}");
                            return;
                        }

                        state.count++;
                    }

                    filterChain.doFilter(request, response);
                } catch (Exception ex) {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
            }

            private String resolveClientIp(HttpServletRequest request) {
                String forwardedFor = request.getHeader("X-Forwarded-For");
                if (forwardedFor != null && !forwardedFor.isBlank()) {
                    return forwardedFor.split(",")[0].trim();
                }

                String realIp = request.getHeader("X-Real-IP");
                if (realIp != null && !realIp.isBlank()) {
                    return realIp.trim();
                }

                return request.getRemoteAddr();
            }
        };
    }

    private static final class WindowState {
        private long windowStartMs = System.currentTimeMillis();
        private int count = 0;
    }
}
