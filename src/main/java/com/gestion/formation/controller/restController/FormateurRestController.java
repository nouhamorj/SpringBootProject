package com.gestion.formation.controller.restController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.formation.entities.Atelier;
import com.gestion.formation.entities.Formateur;
import com.gestion.formation.repositories.FormateurRepository;
import com.gestion.formation.services.ServiceFormateur;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/formateurs")
public class FormateurRestController {
    private ServiceFormateur serviceFormateur;
    private FormateurRepository formateurRepository;

    @GetMapping("/all")
    public List<Formateur> getAllFormateurs() {
        return serviceFormateur.getAllFormateurs(); // Assuming this method exists in your service
    }

    // Get formateur by ID
    @GetMapping("/{id}")
    public Formateur getFormateurById(@PathVariable Long id) {
        return serviceFormateur.getFormateur(id);
    }

    // Delete a formateur
    @DeleteMapping("/delete/{id}")
    public void deleteFormateur(@PathVariable Long id) {
        serviceFormateur.deleteFormateur(id);
    }


    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable Long id) throws IOException {
        Formateur formateur = serviceFormateur.getFormateur(id);
        if (formateur != null && formateur.getNomImage() != null) {
            return serviceFormateur.getImage(id); // Assume you implement this method to get image bytes
        }
        return new byte[0]; // Return an empty byte array if no image found
    }

    @PutMapping("/update-formateur/{id}")
    public Formateur updateFormateur(@PathVariable Long id, @RequestPart Formateur formateur){
        return serviceFormateur.updateFormateur2(id, formateur);
    }


    @PostMapping("/save")
    public void addFormateur(@RequestBody Formateur formateur){
        formateurRepository.save(formateur);

    }

    @GetMapping("/recherche-formateur")
    public List<Formateur> rechercherFormateurParNom(@RequestParam("motCle") String motCle) {
        return serviceFormateur.rechercherFormateursParNom(motCle);
    }

    /*
    @PutMapping("/update-formateur/{id}")
    public Formateur updateFormateur(@PathVariable Long id, @RequestPart Formateur formateur, @RequestPart MultipartFile file) throws Exception {
        return serviceFormateur.updateFormateur(id, formateur, file);
    }


    @PostMapping("/save")
    public void addFormateur(@RequestParam String formateurData, @RequestParam MultipartFile file) throws IOException {
        Formateur formateur = new ObjectMapper().readValue(formateurData, Formateur.class);
        serviceFormateur.ajouterFormateur(formateur, file);
    }*/



}
