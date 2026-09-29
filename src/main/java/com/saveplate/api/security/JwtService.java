package com.saveplate.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HexFormat;

@Service
public class JwtService {
    private static final String RESET_PURPOSE = "password_reset";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.reset-expiration}")
    private long resetExpiration;
    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String generatePasswordResetToken(String email,String currentPasswordHash){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+ resetExpiration);
        return Jwts.builder()
                .subject(email)
                .claim("purpose",RESET_PURPOSE)
                .claim("pwdFingerprint", fingerprint(currentPasswordHash))
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    public boolean isPasswordResetTokenValid(String token, String currentPasswordHash) {
        Claims claims = extractClaims(token);
        boolean purposeMatches = RESET_PURPOSE.equals(claims.get("purpose", String.class));
        boolean fingerprintMatches = fingerprint(currentPasswordHash).equals(claims.get("pwdFingerprint",
                String.class));
        boolean notExpired = claims.getExpiration().after(new Date());
        return purposeMatches && fingerprintMatches && notExpired;
    }
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    private String fingerprint(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
