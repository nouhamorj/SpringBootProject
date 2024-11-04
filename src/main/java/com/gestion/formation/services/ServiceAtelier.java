package com.gestion.formation.services;


import com.gestion.formation.entities.Atelier;
import com.gestion.formation.repositories.AtelierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@AllArgsConstructor
public class ServiceAtelier implements IServiceAtelier {
    private AtelierRepository ar;

    @Override
    public Page<Atelier> rechercherAteliersParTitre(String motCle, Pageable p) {
        return ar.findByTitreContains(motCle, p);
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
    public void ajouterAtelier(Atelier atelier, MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            atelier.setNomPdf(saveFile(multipartFile));
        }
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

    private String saveFile(MultipartFile mf) throws IOException {
        String nomFile = mf.getOriginalFilename();
        String[] tab = nomFile.split("\\.");
        String newName = tab[0] + System.currentTimeMillis() + "." + tab[1];
        String directory = "src/main/resources/static/pdfs";
        File fileDir = new File(directory);
        if (!fileDir.exists()) { //vérifie si le dossier existe sinon le cré
            fileDir.mkdirs();
        }
        Path path = Paths.get(fileDir.getAbsolutePath(), newName);
        Files.write(path, mf.getBytes());
        return newName;

    }


    public byte[] getPdfFile(Long id) throws IOException {
        // Récupérer l'atelier par son ID
        Atelier atelier = getAtelier(id);

        // Vérifiez si l'atelier et le PDF existent
        if (atelier != null && atelier.getNomPdf() != null) {
            // Définir le chemin vers le fichier PDF
            Path pdfPath = Paths.get("src/main/resources/static/pdfs", atelier.getNomPdf());

            // Vérifiez si le fichier existe
            if (Files.exists(pdfPath)) {
                // Lire le contenu du fichier en tant que tableau d'octets
                return Files.readAllBytes(pdfPath);
            }
        }
        return null; // Retourne null si le fichier n'est pas trouvé
    }

}
