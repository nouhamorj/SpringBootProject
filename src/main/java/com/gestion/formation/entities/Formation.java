package com.gestion.formation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFormation;

    @ManyToMany(mappedBy = "formations", cascade = CascadeType.ALL)
    private List<Atelier> ateliers;

}
