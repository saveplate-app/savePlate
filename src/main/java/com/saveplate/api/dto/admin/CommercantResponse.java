package com.saveplate.api.dto.admin;

import com.saveplate.api.entities.enums.Category;

import java.util.UUID;

public record CommercantResponse(
        UUID commercantId,
        String ownerEmail,
        String nomBoutique,
        String adresse,
        Double latitude,
        Double longitude,
        Category categorie,
        String horairesOuverture,
        String photo
) {
}
