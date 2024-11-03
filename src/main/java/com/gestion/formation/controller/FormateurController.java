package com.gestion.formation.controller;
import com.gestion.formation.entities.Formateur;
import com.gestion.formation.services.IServiceFormateur;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
@RequestMapping("/formateurs")
public class FormateurController {
    private IServiceFormateur sf;

    @GetMapping("/liste-formateurs")
    public String listeFormateurs(Model model,
                                  @RequestParam(name = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "size", defaultValue = "2") int size,
                                  @RequestParam(name = "mc", defaultValue = "") String mc) {
        Page<Formateur> listPage = sf.rechercherFormateursParNom(mc, PageRequest.of(page - 1, size));
        model.addAttribute("formateurs", listPage.getContent());
        model.addAttribute("pages", new int[listPage.getTotalPages()]);
        model.addAttribute("current", listPage.getNumber());
        model.addAttribute("mc", mc);
        return "formateurs/liste-formateurs";

    }


    @GetMapping("/{id}")
    public String detailFormateur(@PathVariable Long id, Model model) {
        Formateur f = sf.getFormateur(id);
        if (f != null) {
            model.addAttribute("formateur", f);
            return "formateurs/detail-formateur";
        } else
            return "redirect:/formateurs/liste-formateurs";
    }

    @GetMapping("/ajout-formateur")
    public String AffichageFormulaireAjout(Model model) {
        Formateur formateur = new Formateur();
        model.addAttribute("formateur", formateur);
        return "formateurs/ajoutFormateur";
    }

    @PostMapping("/save")
    public String ajouterFormateur(@ModelAttribute Formateur f, @RequestParam("file") MultipartFile mf) throws Exception {
        sf.ajouterFormateur(f, mf);
        return "redirect:/formateurs/liste-formateurs";
    }

    @GetMapping("/delete/{id}")
    public String supprimerFormateur(@PathVariable Long id) {
        sf.deleteFormateur(id);
        return "redirect:/formateurs/liste-formateurs";
    }

    @GetMapping("/modifier-formateur/{id}")
    public String editFormateur(@PathVariable Long id, Model model) {
        Formateur f = sf.getFormateur(id);
        model.addAttribute("formateur", f);
        return "formateurs/modifFormateur";
    }


    @PostMapping("/save-modification/{id}")
    public String modifierFormateur(@ModelAttribute Formateur f, @PathVariable Long id,
                                    @RequestParam("file") MultipartFile mf) throws Exception {
        sf.updateFormateur(id,f,mf);
        return "redirect:/formateurs/liste-formateurs";
    }
}
