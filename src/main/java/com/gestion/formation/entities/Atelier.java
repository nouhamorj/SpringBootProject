package com.gestion.formation.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class   Atelier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP) // Enregistre la date avec l'heure
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") //Le format de la date
    private Date dateAtelier;
    private String titre;
    private String description;
    private String nomPdf;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "formateur_id") //formateur_id hiya clé étrangère fi table Atelier
    private Formateur formateur;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "atelier_theme",
            joinColumns = @JoinColumn(name = "atelier_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private List<Theme> themes;


}
