package com.snaporia.authService.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snaporia.authService.dto.RefreshTokenSession;
import com.snaporia.authService.exception.TokenExpiredException;
import com.snaporia.authService.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String PREFIX = "refresh:token:";

    @Override
    public String create(String userEmail,String device, String ip, String userAgent) {
        String refreshToken = UUID.randomUUID().toString();

        RefreshTokenSession session = RefreshTokenSession.builder()
                .userEmail(userEmail)
                .device(device)
                .ip(ip)
                .userAgent(userAgent)
                .loginAt(LocalDateTime.now())
                .build();

        redisTemplate.opsForValue().set(
                PREFIX + refreshToken,
                session,
                7,
                TimeUnit.DAYS
        );

        return refreshToken;
    }

    @Override
    public RefreshTokenSession validateAndGetUserEmail(String refreshToken) throws TokenExpiredException {
        Object value =
                redisTemplate.opsForValue().get(PREFIX + refreshToken);

        if (value == null) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        return objectMapper.convertValue(
                value,
                RefreshTokenSession.class
        );
    }

    @Override
    public void delete(String refreshToken) {
        redisTemplate.delete("refresh:token:" + refreshToken);
    }

    @Override
    public void logoutAllDevices(String userEmail) {

        Set<String> keys = redisTemplate.keys(PREFIX + "*");
        if (keys == null) return;

        for (String key : keys) {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                continue;
            }
            RefreshTokenSession session =
                    objectMapper.convertValue(value, RefreshTokenSession.class);

            if (userEmail.equals(session.getUserEmail())) {
                redisTemplate.delete(key);
            }
        }
    }
}
