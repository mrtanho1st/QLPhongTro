package com.minhtan.qlptbackend.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();

        registerCache(manager, "rooms", Duration.ofMinutes(5), 500);
        registerCache(manager, "buildings", Duration.ofMinutes(10), 300);
        registerCache(manager, "districts", Duration.ofMinutes(30), 200);
        registerCache(manager, "typeRooms", Duration.ofMinutes(30), 200);
        registerCache(manager, "amenities", Duration.ofMinutes(30), 200);
        registerCache(manager, "buildingFees", Duration.ofMinutes(15), 200);
        registerCache(manager, "commissions", Duration.ofMinutes(15), 200);
        registerCache(manager, "landmarks", Duration.ofMinutes(10), 300);
        registerCache(manager, "landmarkTypes", Duration.ofHours(1), 200);
        registerCache(manager, "saleOffs", Duration.ofMinutes(15), 200);
        registerCache(manager, "roomSaleOffs", Duration.ofMinutes(10), 250);
        registerCache(manager, "roomAmenities", Duration.ofMinutes(10), 250);
        registerCache(manager, "roomMedia", Duration.ofMinutes(10), 500);

        return manager;
    }

    private void registerCache(CaffeineCacheManager cacheManager, String cacheName, Duration ttl, long maxSize) {
        cacheManager.registerCustomCache(cacheName, Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(ttl)
                .recordStats()
                .build());
    }
}
