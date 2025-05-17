package com.sixmycat.catchy.common.auth;

import com.sixmycat.catchy.common.auth.domain.RefreshToken;
import com.sixmycat.catchy.config.RedisConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@Import(RedisConfig.class)
@TestPropertySource(properties = {
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6379"
})
public class RedisRefreshTokenTest {

    @Autowired
    private RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate;

    private static final String KEY = "test:refreshToken";

    @AfterEach
    void tearDown() {
        refreshTokenRedisTemplate.delete(KEY);
    }

    @Test
    void saveAndLoadRefreshToken() {
        RefreshToken token = RefreshToken.builder()
                .token("sample-refresh-token-1234")
                .build();

        refreshTokenRedisTemplate.opsForValue().set(KEY, token, 10, TimeUnit.MINUTES);

        RefreshToken saved = refreshTokenRedisTemplate.opsForValue().get(KEY);
        assertThat(saved).isNotNull();
        assertThat(saved.getToken()).isEqualTo("sample-refresh-token-1234");
    }
}
