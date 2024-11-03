package com.gestion.formation.controller;

import com.gestion.formation.entities.Formateur;
import com.gestion.formation.entities.Theme;
import com.gestion.formation.services.IServiceAtelier;
import com.gestion.formation.services.IServiceFormateur;
import com.gestion.formation.services.IServiceTheme;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/themes")
public class ThemeController {

    private IServiceTheme serviceTheme;
    private IServiceAtelier serviceAtelier;

    @GetMapping("/liste-des-themes")
    public String liste(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name="size", defaultValue = "2") int size,
                        @RequestParam(name="mc", defaultValue = "") String mc)
    {   Page<Theme> listPage = serviceTheme.rechercherThemesParNom(mc, PageRequest.of(page - 1, size));
        model.addAttribute("themes", listPage.getContent());
        model.addAttribute("pages", new int[listPage.getTotalPages()]);
        model.addAttribute("current", listPage.getNumber());
        model.addAttribute("mc", mc);
        return "themes/liste-des-themes";
    }


    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Theme t=serviceTheme.getThemeById(id);
        if(t != null) {
            model.addAttribute("theme", t);
            return "themes/detail";
        } else {
            return "redirect:/themes/liste-des-themes";
        }
    }

    @GetMapping("/ajout-theme")
    public String ajoutTheme(Model model) {
        Theme t=new Theme();
        model.addAttribute("theme", t);
        return "themes/ajout-theme";

    }

    @PostMapping("/save")
    public String save(@ModelAttribute Theme t) {
        serviceTheme.ajouterTheme(t);
        return "redirect:/themes/liste-des-themes";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        serviceTheme.deleteTheme(id);
        return "redirect:/themes/liste-des-themes";
    }


    @GetMapping("/modif-theme/{id}")
    public String modifTheme(@PathVariable Long id, Model model) {
        Theme t=serviceTheme.getThemeById(id);
        model.addAttribute("theme", t);
        return "themes/modif-theme";
    }

    @PostMapping("/save-modification-theme/{id}")
    public String saveModificationTheme(@PathVariable Long id, @ModelAttribute Theme t) {
        serviceTheme.mettreAJourTheme(id, t);
        return "redirect:/themes/liste-des-themes";
    }

}
