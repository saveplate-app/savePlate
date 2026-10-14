package com.saveplate.api.mapper;

import com.saveplate.api.dto.admin.CommercantResponse;
import com.saveplate.api.dto.admin.PromoteCommercantRequest;
import com.saveplate.api.entities.Commercant;
import com.saveplate.api.entities.User;
import org.springframework.stereotype.Component;


@Component
public class CommercantMapper {

    public Commercant toEntity(PromoteCommercantRequest request, User user){

        Commercant commercant = new Commercant();
        commercant.setUser(user);
        commercant.setNomBoutique(request.nomBoutique());
        commercant.setAdresse(request.adresse());
        commercant.setLatitude(request.latitude());
        commercant.setLongitude(request.longitude());
        commercant.setCategorie(request.categorie());
        commercant.setHorairesOuverture(request.horairesOuverture());
        commercant.setPhoto(request.photo());
        return commercant;
    }

    public CommercantResponse toResponse(Commercant commercant)
    {
        return new CommercantResponse(
                commercant.getCommercantId(),
                commercant.getUser().getEmail(),
                commercant.getNomBoutique(),
                commercant.getAdresse(),
                commercant.getLatitude(),
                commercant.getLongitude(),
                commercant.getCategorie(),
                commercant.getHorairesOuverture(),
                commercant.getPhoto()

        );
    }



}
