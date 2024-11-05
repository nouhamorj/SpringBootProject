package com.gestion.formation.controller.restController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.formation.entities.Atelier;
import com.gestion.formation.repositories.AtelierRepository;
import com.gestion.formation.services.ServiceAtelier;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ateliers")
public class AtelierRestController {
    private ServiceAtelier serviceAtelier;
    private AtelierRepository atelierRepository;
    @GetMapping
    public List<Atelier> getAllAteliers() {
        return serviceAtelier.getAteliers(); // Vous devez avoir cette méthode dans votre service
    }

    // Récupérer un atelier par son ID
    @GetMapping("/{id}")
    public Atelier getAtelier(@PathVariable Long id) {
        return serviceAtelier.getAtelier(id);
    }

    /*@PostMapping("/save")
    public String addAtelier(@RequestBody Atelier atelier, @RequestParam MultipartFile pdf) throws IOException {
        serviceAtelier.ajouterAtelier(atelier, pdf);
        return "Atelier ajouté avec succès";
    }*/


    @PostMapping("/update/{id}")
    public String updateAtelier(@PathVariable Long id, @RequestBody Atelier atelier) {
        serviceAtelier.updateAtelier(id, atelier);
        return "Atelier modifié avec succès";
    }

    // Supprimer un atelier
    @DeleteMapping("/delete/{id}")
    public String deleteAtelier(@PathVariable Long id) {
        serviceAtelier.deleteAtelier(id);
        return "Atelier supprimé avec succès"; // Message de succès
    }

    // Recherche d'ateliers par thème
    @GetMapping("/recherche-theme")
    public List<Atelier> rechercherAteliersParTheme(@RequestParam("motCle") String motCle) {
        return serviceAtelier.rechercherAteliersParTheme(motCle);
    }


    @GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPdfForAtelier(@PathVariable Long id) throws IOException {
        Atelier atelier = serviceAtelier.getAtelier(id);

        // Check if the Atelier has an associated PDF file
        if (atelier != null && atelier.getNomPdf() != null) {
            byte[] pdfBytes = serviceAtelier.getPdfFile(id); // Implement this method to retrieve PDF bytes

            // Return the PDF byte array if found
            if (pdfBytes != null) {
                return pdfBytes;
            }
        }

        // Return an empty byte array if no PDF is found
        return new byte[0];
    }


    /*@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addAtelier(@ModelAttribute Atelier atelier, @RequestParam("pdf") MultipartFile pdf
    ) throws IOException {
        // Appel du service pour ajouter l'atelier avec le fichier PDF
        serviceAtelier.ajouterAtelier(atelier, pdf);
        return "Atelier ajouté avec succès";
    }*/

    @PostMapping(value = "/save")
    public String addAtelier(@RequestBody Atelier atelier ){
        atelierRepository.save(atelier);
        return "Atelier ajouté avec succès";
    }

}
