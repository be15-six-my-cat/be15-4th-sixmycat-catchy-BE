package com.sixmycat.catchy.common.auth;

import com.sixmycat.catchy.common.auth.domain.RefreshToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class RedisRefreshTokenTest {

    private RedisTemplate<String, RefreshToken> redisTemplate;

    private static final String KEY = "test:refreshToken";

    @BeforeEach
    void setup() {
        // GitHub Actions에 설정한 Redis 환경변수와 일치하게 지정
        String redisHost = System.getenv().getOrDefault("REDIS_HOST", "localhost");
        int redisPort = Integer.parseInt(System.getenv().getOrDefault("REDIS_PORT", "6379"));

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(config);
        connectionFactory.afterPropertiesSet(); // 중요

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet(); // 중요
    }

    @AfterEach
    void tearDown() {
        redisTemplate.delete(KEY);
    }

    @Test
    void saveAndLoadRefreshToken() {
        RefreshToken token = RefreshToken.builder()
                .token("sample-refresh-token-1234")
                .build();

        redisTemplate.opsForValue().set(KEY, token, 10, TimeUnit.MINUTES);

        RefreshToken saved = redisTemplate.opsForValue().get(KEY);
        assertThat(saved).isNotNull();
        assertThat(saved.getToken()).isEqualTo("sample-refresh-token-1234");
    }
}
