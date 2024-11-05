package com.gestion.formation.services;

import com.gestion.formation.entities.Atelier;
import com.gestion.formation.entities.Theme;
import com.gestion.formation.repositories.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceTheme implements IServiceTheme{
    private ThemeRepository tr;

    @Override
    public Page<Theme> rechercherThemesParNom(String motCle, Pageable pageable) {
        return tr.findByNomContains(motCle,pageable);
    }

    public List<Theme> rechercherThemesParNom2(String motCle) {
        return tr.findByNomContains(motCle);
    }

    @Override
    public Theme ajouterTheme(Theme theme) {
        tr.save(theme);
        return theme;
    }

    @Override
    public Theme mettreAJourTheme(Long id, Theme theme) {
        Theme existingtheme = tr.findById(id).orElse(null);
        existingtheme.setNom(theme.getNom());
        tr.save(existingtheme);
        return existingtheme;
    }

    @Override
    public void deleteTheme(Long id) {
        tr.deleteById(id);
    }

    @Override
    public Theme getThemeById(Long id) {
        return tr.findById(id).orElse(null);
    }

    @Override
    public List<Theme> getAllthemes() {
        return tr.findAll();
    }


}
