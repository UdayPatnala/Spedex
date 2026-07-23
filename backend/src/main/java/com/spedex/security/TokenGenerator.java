package com.spedex.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class TokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Generates a cryptographically secure random token string.
     */
    public String generateSecureToken(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Generates a list of secure recovery codes in standard 8-char chunk format.
     */
    public List<String> generateRecoveryCodes(int count) {
        List<String> codes = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String part1 = UUID.randomUUID().toString().substring(0, 8);
            String part2 = UUID.randomUUID().toString().substring(0, 8);
            codes.add(part1 + "-" + part2);
        }
        return codes;
    }
}
