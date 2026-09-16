package com.saveplate.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email est invalide")
        String email,
        @NotBlank(message = "Le password est obligatoire")
        String password

) {}

