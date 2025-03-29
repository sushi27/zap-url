package com.susanto.zap_url.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    StringRedisTemplate redisTemplate;

    private static final long EXPIRATION_TIME = 24; // Cache expiry in hours

    public void saveUrlToCache(String key, String value) {
        redisTemplate.opsForValue().set(key, value, EXPIRATION_TIME, TimeUnit.HOURS);
    }

    public String getUrlFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
