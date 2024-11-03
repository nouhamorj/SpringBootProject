package com.gestion.formation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Atelier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;

    @ManyToOne
    @JoinColumn(name = "formateur_id")
    private Formateur formateur;

    @ManyToMany
    @JoinTable(
            name = "atelier_theme",
            joinColumns = @JoinColumn(name = "atelier_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private List<Theme> themes;

    @ManyToMany
    @JoinTable(
            name = "formation_atelier",
            joinColumns = @JoinColumn(name = "atelier_id"),
            inverseJoinColumns = @JoinColumn(name = "formation_id")
    )
    private List<Formation> formations;

    @OneToMany(mappedBy = "atelier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentPDF> documentsPDF; // Liste des documents PDF associés à l'atelier
}
