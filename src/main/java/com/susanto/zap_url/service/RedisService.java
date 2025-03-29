package com.susanto.zap_url.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    StringRedisTemplate redisTemplate;

    private static final long EXPIRATION_TIME = 7; // Cache expiry for less-used URLs (days)
    private static final int RATE_LIMIT = 10; // Max requests per minute

    public boolean isRateLimited(String userIp) {
        String key = "rate_limit:" + userIp;
        long requestCount = redisTemplate.opsForValue().increment(key);

        if (requestCount == 1) {
            // First request → Set expiry (1 minute)
            redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        }

        return requestCount > RATE_LIMIT;
    }

    public void saveShortToLong(String shortCode, String longUrl) {
        redisTemplate.opsForValue().set(shortCode, longUrl);
    }

    public void saveLongToShort(String longUrl, String shortCode) {
        redisTemplate.opsForValue().set(longUrl, shortCode, EXPIRATION_TIME, TimeUnit.DAYS);
    }

    public String getUrlFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
