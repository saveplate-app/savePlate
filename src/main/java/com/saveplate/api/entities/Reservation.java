package com.saveplate.api.entities;

import com.saveplate.api.entities.enums.StatusReservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID reservationId = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "panier_id", nullable = false)
    private Panier panier;

    private int quantiteReservee;

    @Enumerated(EnumType.STRING)
    private StatusReservation statusReservation;

    private LocalDateTime dateReservation;

    private String qrCode;
}
