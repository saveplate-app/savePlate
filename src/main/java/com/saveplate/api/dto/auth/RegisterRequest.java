package com.saveplate.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Le prénom est obligatoire")
        String firstname,
        @NotBlank(message = "Le nom est obligatoire")
        String lastName,
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "format d'email est invalide")
        String email,
        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min=8,message = "Le mot de passe doit contenir au moins 8 caractères")
        String password
) {
}
