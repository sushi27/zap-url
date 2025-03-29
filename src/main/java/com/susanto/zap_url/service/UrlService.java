package com.susanto.zap_url.service;

import com.susanto.zap_url.entity.UrlMapping;
import com.susanto.zap_url.repository.UrlRepository;
import com.susanto.zap_url.util.Base62Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UrlService {
    @Autowired
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(String longUrl) {
        Optional<UrlMapping> existing = urlRepository.findByLongUrl(longUrl);
        if (existing.isPresent()) {
            return existing.get().getShortCode();
        }

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping = urlRepository.save(urlMapping);

        String shortCode = Base62Encoder.encode(urlMapping.getId());

        urlMapping.setShortCode(shortCode);
        urlRepository.save(urlMapping);

        return shortCode;
    }

    public String getLongUrl(String shortCode) {
        long id = Base62Encoder.decode(shortCode);
        return urlRepository.findById(id).map(UrlMapping::getLongUrl).orElse(null);
    }
}
