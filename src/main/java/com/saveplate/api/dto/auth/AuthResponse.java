package com.saveplate.api.dto.auth;

public record AuthResponse(

        String token,
        String email,
        String role
) {
}
