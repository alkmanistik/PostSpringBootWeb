package com.alkmanistik.springalkmanistikwebsite.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfiguration {

    @Bean
    public CacheManager redisCacheManager(JedisConnectionFactory connectionFactory) {
        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> cacheConfigurations = Map.of(
                "post", defaultConfig.entryTtl(Duration.ofMinutes(10)),
                "posts", defaultConfig.entryTtl(Duration.ofMinutes(5)),
                "comment", defaultConfig.entryTtl(Duration.ofMinutes(15)),
                "commentByPostId", defaultConfig.entryTtl(Duration.ofMinutes(20))
        );
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

}
