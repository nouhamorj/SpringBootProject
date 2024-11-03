package com.gestion.formation.controller;

import com.gestion.formation.entities.Atelier;
import com.gestion.formation.services.IServiceAtelier;
import com.gestion.formation.services.IServiceDocumentPDF;
import com.gestion.formation.services.IServiceFormateur;
import com.gestion.formation.services.IServiceTheme;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/ateliers")
public class AtelierController {
    private IServiceAtelier serviceAtelier;
    private IServiceTheme serviceTheme;
    private IServiceFormateur serviceFormateur;
    private IServiceDocumentPDF serviceDocumentPDF;

    // Afficher tous les ateliers
    @GetMapping(path = {"/liste"})
    public String afficherAteliers(Model model,
                                   @RequestParam(name = "page" , defaultValue = "1") int page,
                                   @RequestParam(name="size", defaultValue = "2") int size,
                                   @RequestParam(name="mc", defaultValue = "")String mc) {
        Page<Atelier> listPage= serviceAtelier.rechercherAteliersParTitre(mc, PageRequest.of(page-1,size));
        model.addAttribute("ateliers", listPage.getContent());
        model.addAttribute("pages", new int[listPage.getTotalPages()]);
        model.addAttribute("current", listPage.getNumber());
        model.addAttribute("mc", mc);
        return "ateliers/liste";
    }

    // Afficher un atelier par son ID
    @GetMapping("/{id}")
    public String afficherAtelier(@PathVariable Long id, Model model) {
        Atelier atelier = serviceAtelier.getAtelier(id);
        if (atelier != null) {
            model.addAttribute("atelier", atelier);
            return "ateliers/details"; // Vue pour les détails d'un atelier
        } else {
            return "redirect:/ateliers"; // Redirige si l'atelier n'existe pas
        }
    }

    // Recherche par thème
    @GetMapping("/recherche-theme")
    public String rechercherAteliersParTheme(@RequestParam("motCle") String motCle, Model model) {
        List<Atelier> ateliers = serviceAtelier.rechercherAteliersParTheme(motCle);
        model.addAttribute("ateliers", ateliers);
        model.addAttribute("motCle", motCle); // Pour afficher le mot clé recherché
        return "ateliers/liste";
    }


    @GetMapping({"/ajouter"})
    public String afficherFormulaire(Model model) {
        Atelier atelier = new Atelier();
        model.addAttribute("atelier", atelier);
        model.addAttribute("allThemes", serviceTheme.getAllthemes());
        model.addAttribute("allFormateurs", serviceFormateur.getAllFormateurs());
        return "ateliers/formulaire";
    }


    @PostMapping("/save")
    public String ajouterAtelier(@ModelAttribute Atelier atelier, @RequestParam("pdfFiles") MultipartFile[] pdfFiles) throws IOException {
        try {
            serviceAtelier.ajouterAtelier(atelier, pdfFiles);
            return "redirect:/ateliers/liste"; // Redirection après succès
        } catch (Exception e) {
            // Gérer l'erreur en affichant un message ou redirigez vers une page d'erreur
            e.printStackTrace(); // Pour le débogage, vous pouvez améliorer le traitement des erreurs
            return "redirect:/ateliers/error"; // Par exemple, une page d'erreur
        }
    }

    @GetMapping(path = "modifier/{id}")
    public String editAtelier(@PathVariable Long id, Model model) {
        Atelier atelier = id != null ? serviceAtelier.getAtelier(id) : new Atelier();
        model.addAttribute("atelier", atelier);
        model.addAttribute("allThemes", serviceTheme.getAllthemes());
        model.addAttribute("allFormateurs", serviceFormateur.getAllFormateurs());
        return "ateliers/edit";
    }



    @PostMapping("/modif/{id}")
    public String modifierAtelier(@ModelAttribute Atelier atelier, @PathVariable Long id) {
        serviceAtelier.updateAtelier(id, atelier);
        return "redirect:/ateliers/liste";
    }

    @GetMapping("/delete/{id}")
    public String supprimerAteier(@PathVariable Long id) {
        serviceAtelier.deleteAtelier(id);
        return "redirect:/ateliers/liste";
    }

}
