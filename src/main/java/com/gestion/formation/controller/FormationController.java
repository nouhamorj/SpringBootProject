package com.gestion.formation.controller;

import com.gestion.formation.entities.Atelier;
import com.gestion.formation.entities.Formation;
import com.gestion.formation.services.IServiceAtelier;
import com.gestion.formation.services.IServiceFormation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



@Controller
@AllArgsConstructor
 @RequestMapping("/formations")
public class FormationController {
    private IServiceFormation serviceFormation;
    private IServiceAtelier serviceAtelier;

    @GetMapping(path="/liste-formations")
    public String afficherListeFormations(Model model,
                                          @RequestParam(name="page", defaultValue = "1") int page,
                                          @RequestParam(name="size", defaultValue = "2") int size,
                                          @RequestParam(name="mc", defaultValue = "") String mc) {
        Page<Formation> listPage= serviceFormation.rechercherFormationsParTitre(mc, PageRequest.of(page-1,size));
        model.addAttribute("formations", listPage.getContent());
        model.addAttribute("pages", new int[listPage.getTotalPages()]);
        model.addAttribute("current", listPage.getNumber());
        model.addAttribute("mc", mc);
        return "formations/liste-formations";

    }
    @GetMapping(path="/{id}")
    public String afficherFormation(@PathVariable Long id, Model model) {
        Formation f=serviceFormation.getFormationById(id);
        if(f != null){
            model.addAttribute("formation", f);
            return "formations/formation-details";
        } else {
            return "redirect:/formations/liste-formations";
        }
    }

    @GetMapping(path="/ajouter")
    public String formuliareAjout(Model model) {
        Formation f=new Formation();
        model.addAttribute("formation", f);
        model.addAttribute("allAteliers", serviceAtelier.getAteliers());
        return "formations/formulaire-ajout";
    }

    @PostMapping("/save")
    public String ajouterFormation(@ModelAttribute Formation formation,
                                   @RequestParam(name = "ateliers", required = false) List<Long> ateliers) {
        if (ateliers != null) {
            List<Atelier> ateliersSelectionnes = new ArrayList<>();
            for (Long atelierId : ateliers) {
                Atelier atelier = serviceAtelier.getAtelier(atelierId);
                if (atelier != null) {
                    ateliersSelectionnes.add(atelier);

                    // Ajouter la formation à chaque atelier pour gérer la relation bidirectionnelle
                    atelier.getFormations().add(formation);
                }
            }
            formation.setAteliers(ateliersSelectionnes);
        }
        serviceFormation.ajouterFormation(formation);
        return "redirect:/formations/liste-formations";
    }

    @GetMapping("/modifier-formation/{id}")
    public String formulaireModification(@PathVariable Long id, Model model) {
        Formation f = id != null ? serviceFormation.getFormationById(id) : new Formation();
        model.addAttribute("formation", f);
        model.addAttribute("allAteliers", serviceAtelier.getAteliers());
        return "formations/formation-modif";
    }

    @PostMapping("/save-modification/{id}")
    public String modifierFormation(@ModelAttribute Formation formation, @PathVariable Long id,
                                    @RequestParam(name = "ateliers", required = false) List<Long> ateliersIds) {
        serviceFormation.updateFormation(id, formation, ateliersIds);
        return "redirect:/formations/liste-formations";
    }

    @GetMapping("/delete/{id}")
    public String supprimerFormation(@PathVariable Long id) {
        serviceFormation.supprimerFormation(id);
        return "redirect:/formations/liste-formations";
    }

}
