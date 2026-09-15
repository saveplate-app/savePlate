package com.saveplate.api.entities;

import com.saveplate.api.entities.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "commercants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Commercant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID commercantId = UUID.randomUUID();

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String nomBoutique;
    private String adresse;
    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private Category categorie;

    private String photo;
    private String horairesOuverture;

    @OneToMany(mappedBy = "commercant")
    private List<Panier> paniers;
}
