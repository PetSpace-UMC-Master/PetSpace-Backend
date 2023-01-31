package com.petspace.dev.service;

import com.petspace.dev.service.auth.jwt.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate redisTemplate;

    public void save(String email, Token token) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = token.getRefreshToken();
        Duration expired = Duration.ofMillis(token.getRefreshTokenExpiredIn());
        valueOperations.set(email, refreshToken, expired);
    }

    public String getValue(String email){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(email);
    }
}
