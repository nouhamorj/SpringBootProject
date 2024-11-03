package com.gestion.formation.services;


import com.gestion.formation.entities.Atelier;
import com.gestion.formation.entities.DocumentPDF;
import com.gestion.formation.repositories.AtelierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ServiceAtelier implements IServiceAtelier {
    private AtelierRepository ar;
    private ServiceDocumentPDF service;
    @Override
    public Page<Atelier> rechercherAteliersParTitre(String motCle, Pageable p) {
        return ar.findByTitreContains(motCle,p);
    }

    @Override
    public List<Atelier> rechercherAteliersParTheme(String motCle) {
        return ar.findByThemeNomContains(motCle);
    }

    @Override
    public List<Atelier> getAteliers() {
        return ar.findAll();
    }

    @Override
    public void ajouterAtelier(Atelier atelier, MultipartFile[] pdfFiles) throws IOException {
        // Enregistrer l'atelier d'abord
        atelier = ar.save(atelier);

        if (pdfFiles != null && pdfFiles.length > 0) {
            for (MultipartFile pdfFile : pdfFiles) {
                if (!pdfFile.isEmpty()) {
                    // Appel de la méthode uploadPDF pour enregistrer le fichier et obtenir l'objet DocumentPDF
                    DocumentPDF document = service.uploadPDF(pdfFile, atelier);

                    // Ajouter le document à l'atelier
                    atelier.getDocumentsPDF().add(document);
                }
            }
        }

        // Mettre à jour l'atelier avec les documents
        ar.save(atelier);
    }
    @Override
    public void updateAtelier(Long id, Atelier atelier) {
        Atelier existingAtelier = ar.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atelier not found with id: " + id));
        existingAtelier.setTitre(atelier.getTitre());
        existingAtelier.setDescription(atelier.getDescription());
        existingAtelier.setFormateur(atelier.getFormateur());
        existingAtelier.setThemes(atelier.getThemes());
        ar.save(existingAtelier);
    }

    @Override
    public void deleteAtelier(Long id) {
        ar.deleteById(id);

    }

    @Override
    public Atelier getAtelier(Long id) {
        return ar.findById(id).orElse(null);
    }

    private void saveFile(MultipartFile file, String path) throws IOException {
        File destFile = new File(path);
        file.transferTo(destFile); // Transférer le fichier sur le serveur
    }
}
