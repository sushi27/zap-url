package com.susanto.zap_url.controller;

import com.susanto.zap_url.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<Map<String, String>> shortenUrl(@RequestBody Map<String, String> request) {
        String longUrl = request.get("longUrl");
        String shortCode = urlService.shortenUrl(longUrl);
        String shortUrl = "http://localhost:8080/" + shortCode;
        return ResponseEntity.ok(Map.of("shortUrl", shortUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Object> redirectToOriginal(@PathVariable String shortCode) {
        return urlService.getLongUrl(shortCode)
                .map(url -> ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}