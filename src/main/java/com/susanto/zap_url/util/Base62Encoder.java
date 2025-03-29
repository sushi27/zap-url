package com.susanto.zap_url.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Base62Encoder {
    private static final String BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // Convert number to Base62
    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62.charAt((int) (num % 62)));
            num /= 62;
        }
        return sb.reverse().toString();
    }

    // Convert hash to Base62 (6-7 characters)
    public static String hashToBase62(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert first 8 bytes of hash to a long number
            long hashLong = 0;
            for (int i = 0; i < 8; i++) {
                hashLong = (hashLong << 8) | (hash[i] & 0xFF);
            }

            return encode(Math.abs(hashLong)).substring(0, 7); // Ensure 6-7 characters
        } catch (Exception e) {
            throw new RuntimeException("Hashing error", e);
        }
    }
}
