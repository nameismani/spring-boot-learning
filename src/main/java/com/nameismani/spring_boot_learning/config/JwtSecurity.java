package com.nameismani.spring_boot_learning.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.nameismani.spring_boot_learning.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtSecurity {
    private static final String SECRET_KEY = "hG9ZxKQyF8eZP7VwA9bR3Xn2JkLmQWEr";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_MS = 60 * 60 * 1000;

    // ✅ Store ONLY userId (NO username/email)
    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .claim("userId", user.getId())      // ✅ ONLY userId
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(KEY)
                .compact();
    }

    // ✅ Extract userId (no username needed)
    public Long extractUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    // ✅ Validate by userId + email match
public boolean validateToken(String token, UserEntity user) {
    try {
        Long tokenUserId = extractUserId(token);
        // ✅ Check 1: userId matches
        if (tokenUserId == null || !tokenUserId.equals(user.getId())) {
            return false;
        }
        
        // ✅ Check 2: NOT expired (NOW USED!)
        if (isTokenExpired(token)) {
            return false;
        }
        
        return true;
    } catch (Exception e) {
        return false;
    }
}

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
