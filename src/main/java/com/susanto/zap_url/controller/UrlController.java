package com.susanto.zap_url.controller;

import com.susanto.zap_url.exception.RateLimitException;
import com.susanto.zap_url.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shortenUrl(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String userIp = httpRequest.getRemoteAddr(); // Get client IP
        String longUrl = request.get("longUrl");
        try {
            String shortCode = urlService.shortenUrl(longUrl, userIp);
            String shortUrl = "http://localhost:8080/" + shortCode;
            return ResponseEntity.ok(Map.of("shortUrl", shortUrl));
        } catch (RateLimitException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
        }
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Object> redirectToOriginal(@PathVariable String shortCode) {
        return urlService.getLongUrl(shortCode)
                .map(url -> ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}