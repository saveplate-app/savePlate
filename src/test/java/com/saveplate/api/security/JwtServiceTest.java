package com.saveplate.api.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "404E635266546A576E5A7234753778214125442A472D4B6150645367566B59");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
        ReflectionTestUtils.setField(jwtService, "resetExpiration", 900000L);
    }

    @Test
    void isPasswordResetTokenValid_shouldReturnTrue_whenPasswordHashUnchangedSinceGeneration() {
        String token = jwtService.generatePasswordResetToken("saad@gmail.com", "hashedPassword");

        assertThat(jwtService.extractEmail(token)).isEqualTo("saad@gmail.com");
        assertThat(jwtService.isPasswordResetTokenValid(token, "hashedPassword")).isTrue();
    }

    @Test
    void isPasswordResetTokenValid_shouldReturnFalse_whenPasswordHashChangedSinceGeneration() {
        String token = jwtService.generatePasswordResetToken("saad@gmail.com", "oldHashedPassword");

        boolean valid = jwtService.isPasswordResetTokenValid(token, "newHashedPassword");

        assertThat(valid).isFalse();
    }

    @Test
    void isPasswordResetTokenValid_shouldReturnFalse_whenTokenHasNoResetPurposeClaim() {
        String loginToken = jwtService.generateToken("saad@gmail.com", "CLIENT");

        boolean valid = jwtService.isPasswordResetTokenValid(loginToken, "hashedPassword");

        assertThat(valid).isFalse();
    }

    @Test
    void isPasswordResetTokenValid_shouldThrowExpiredJwtException_whenTokenExpirationHasPassed() {
        ReflectionTestUtils.setField(jwtService, "resetExpiration", -1000L);
        String token = jwtService.generatePasswordResetToken("saad@gmail.com", "hashedPassword");

        assertThrows(ExpiredJwtException.class,
                () -> jwtService.isPasswordResetTokenValid(token, "hashedPassword"));
    }
}
