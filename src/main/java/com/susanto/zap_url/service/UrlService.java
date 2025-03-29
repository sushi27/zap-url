package com.susanto.zap_url.service;

import com.susanto.zap_url.entity.UrlMapping;
import com.susanto.zap_url.repository.UrlRepository;
import com.susanto.zap_url.util.Base62Encoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(String longUrl) {
        Optional<UrlMapping> existing = urlRepository.findByLongUrl(longUrl);
        if (existing.isPresent()) {
            return existing.get().getShortCode();
        }

        // Step 2: Generate a deterministic Base62 hash
        String shortCode = Base62Encoder.hashToBase62(longUrl);

        // Step 3: Save the new mapping
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setShortCode(shortCode);
        urlRepository.save(urlMapping);

        return shortCode;
    }

    public Optional<String> getLongUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl);
    }
}
