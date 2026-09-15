package com.saveplate.api.entities;

import com.saveplate.api.entities.enums.StatusPanier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "paniers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID panierId = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "commercant_id", nullable = false)
    private Commercant commercant;

    private String description;
    private BigDecimal prixReduit;
    private BigDecimal valeurEstimee;
    private int quantiteInitiale;
    private int quantiteRestante;
    private LocalTime heureDebutRetrait;
    private LocalTime heureFinRetrait;
    private LocalDate dateDisponibilite;

    @Enumerated(EnumType.STRING)
    private StatusPanier status;

    @OneToMany(mappedBy = "panier")
    private List<Reservation> reservations;
}
