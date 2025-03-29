package com.susanto.zap_url.service;

import com.susanto.zap_url.entity.UrlMapping;
import com.susanto.zap_url.exception.RateLimitException;
import com.susanto.zap_url.repository.UrlRepository;
import com.susanto.zap_url.util.Base62Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    @Autowired
    private RedisService redisService;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(String longUrl, String userIp) {
        if (redisService.isRateLimited(userIp)) {
            throw new RateLimitException("Too many requests! Try again later.");
        }

        // Step 1: Check Redis cache
        String cachedShortCode = redisService.getUrlFromCache(longUrl);
        if (cachedShortCode != null) {
            return cachedShortCode; // Return cached short code
        }

        // Step 2: Check if URL already exists in DB
        Optional<UrlMapping> existingMapping = urlRepository.findByLongUrl(longUrl);
        if (existingMapping.isPresent()) {
            String shortCode = existingMapping.get().getShortCode();
            redisService.saveLongToShort(longUrl, shortCode); // Cache with expiry
            return shortCode;
        }

        // Step 3: Generate a new short URL
        String shortCode = Base62Encoder.hashToBase62(longUrl);
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setShortCode(shortCode);
        urlRepository.save(urlMapping);

        // Step 4: Store new short code in Redis
        redisService.saveShortToLong(shortCode, longUrl);  // Permanent cache
        redisService.saveLongToShort(longUrl, shortCode);  // Expiring cache

        return shortCode;
    }

    public Optional<String> getLongUrl(String shortCode) {
        String cachedUrl = redisService.getUrlFromCache(shortCode);

        if (cachedUrl != null) {
            return Optional.of(cachedUrl); // Return cached URL
        }

        // Step 2: If not in cache, fetch from DB and store in Redis
        Optional<String> longUrl = urlRepository.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl);

        longUrl.ifPresent(url -> redisService.saveShortToLong(shortCode, url)); // Cache it permanently

        return longUrl;
    }
}
