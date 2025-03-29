package com.susanto.zap_url.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "url_mapping")
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 10)
    private String shortCode;

    @Column(nullable = false, columnDefinition = "TEXT", unique = true)
    private String longUrl;

    private LocalDateTime createdAt = LocalDateTime.now();
}