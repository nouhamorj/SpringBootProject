package com.gestion.formation.controller.restController;

import com.gestion.formation.entities.Atelier;
import com.gestion.formation.entities.Theme;
import com.gestion.formation.services.ServiceTheme;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/themes")
public class ThemeRestController {
    private ServiceTheme serviceTheme;

    @GetMapping("/all")
    public List<Theme> getAllThemes() {
        return serviceTheme.getAllthemes(); // Assuming this method exists in your service
    }

    // Get theme by ID
    @GetMapping("/{id}")
    public Theme getThemeById(@PathVariable Long id) {
        return serviceTheme.getThemeById(id);
    }

    // Create a new theme
    @PostMapping("/save")
    public Theme createTheme(@RequestBody Theme theme) {
        return serviceTheme.ajouterTheme(theme);
    }

    // Update an existing theme
    @PutMapping("/update/{id}")
    public Theme updateTheme(@PathVariable Long id, @RequestBody Theme theme) {
        return serviceTheme.mettreAJourTheme(id, theme);
    }

    // Delete a theme
    @DeleteMapping("/delete/{id}")
    public void deleteTheme(@PathVariable Long id) {
        serviceTheme.deleteTheme(id);
    }


}
