package com.gestion.formation.controller.restController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.formation.entities.Atelier;
import com.gestion.formation.services.ServiceAtelier;
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
    @GetMapping
    public List<Atelier> getAllAteliers() {
        return serviceAtelier.getAteliers(); // Vous devez avoir cette méthode dans votre service
    }

    // Récupérer un atelier par son ID
    @GetMapping("/{id}")
    public Atelier getAtelier(@PathVariable Long id) {
        return serviceAtelier.getAtelier(id);
    }

    @PostMapping("/save")
    public String addAtelier(@RequestBody Atelier atelier, @RequestParam MultipartFile pdf) throws IOException {
        serviceAtelier.ajouterAtelier(atelier, pdf);
        return "Atelier ajouté avec succès";
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

    public ResponseEntity<byte[]> getPdfForAtelier(@PathVariable Long id) throws IOException {
        try {
            Atelier atelier = serviceAtelier.getAtelier(id);
            if (atelier != null && atelier.getNomPdf() != null) {
                byte[] pdfBytes = serviceAtelier.getPdfFile(id);
                if (pdfBytes != null) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
                }
            }
            return ResponseEntity.notFound().build(); // Handle not found case gracefully
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Handle internal server error
        }
    }

}
