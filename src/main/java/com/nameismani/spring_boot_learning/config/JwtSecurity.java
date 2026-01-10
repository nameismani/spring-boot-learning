package com.nameismani.spring_boot_learning.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nameismani.spring_boot_learning.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtSecurity {

    // MUST be >= 32 chars for HS256
    private static final String SECRET_KEY =
        "hG9ZxKQyF8eZP7VwA9bR3Xn2JkLmQWEr";

    private static final SecretKey KEY =
        Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private static final long EXPIRATION_MS = 60 * 60 * 1000; // 1 hour

    // 🔹 Generate JWT
    public String generateToken(UserDetails userDetails) {
          UserEntity user = (UserEntity) userDetails;
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId", user.getId())  // ✅ User ID
                .claim("roles", user.getRole()) // ✅ Role
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(KEY)
                .compact();
    }

    // 🔹 Extract username
    public String extractUsername(String token) {

        return getClaims(token).getSubject();
    }

    // 🔹 Validate token
    public boolean validateToken(String token, UserDetails userDetails) {

        String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
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
