package com.gestion.formation.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentPDF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom; // Nom du document
    private String chemin; // Chemin d'accès au document sur le serveur ou URL

    @ManyToOne
    @JoinColumn(name = "atelier_id")
    private Atelier atelier; // Référence à l'atelier auquel le document est associé
}
