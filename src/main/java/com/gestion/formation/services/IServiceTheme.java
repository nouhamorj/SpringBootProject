package com.gestion.formation.services;

import com.gestion.formation.entities.Atelier;
import com.gestion.formation.entities.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IServiceTheme {
    Page<Theme> rechercherThemesParNom(String motCle, Pageable pageable);
    public Theme ajouterTheme(Theme theme);
    public Theme mettreAJourTheme(Long id, Theme theme);
    public void deleteTheme(Long id);
    public Theme getThemeById(Long id);
    public List<Theme> getAllthemes();
}
