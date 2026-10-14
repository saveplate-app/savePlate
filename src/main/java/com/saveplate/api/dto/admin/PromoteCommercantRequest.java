package com.saveplate.api.dto.admin;

import com.saveplate.api.entities.enums.Category;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PromoteCommercantRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Le format d'email est invalide")
        String email,
        @NotBlank(message = "Le nom de la boutique est obligatoire")
        String nomBoutique,
        @NotBlank(message = "L'adresse est obligatoire")
        String adresse,
        Double latitude,
        Double longitude,
        @NotNull(message = "La catégorie est obligatoire")
        Category categorie,
        String horairesOuverture,
        String photo


) {
}
