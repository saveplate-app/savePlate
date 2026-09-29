package com.saveplate.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



public record ResetPasswordRequest(
        @NotBlank(message = "Le token est obligatoire")
        String token,
        @NotBlank(message = "le mot de passe est obligatoire")
        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
        String newPassword
) {
}
