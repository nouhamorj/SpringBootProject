package com.gestion.formation.services;

import com.gestion.formation.entities.Formateur;
import com.gestion.formation.repositories.FormateurRepository;
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
@AllArgsConstructor
@Service
public class ServiceFormateur implements IServiceFormateur {
    private FormateurRepository fr;
    @Override
    public Page<Formateur> rechercherFormateursParNom(String motCle, Pageable pageable) {
        return fr.findByNomContains(motCle,pageable);
    }

    @Override
    public List<Formateur> rechercherFormateursParNom(String motCle) {
        return fr.findByNomContains(motCle);
    }

    @Override
    public Formateur ajouterFormateur(Formateur formateur, MultipartFile file) throws IOException {
        if (!file.isEmpty()){
            formateur.setNomImage(saveImage(file));
        }
        fr.save(formateur);
        return formateur;
    }

    @Override
    public Formateur updateFormateur(Long id, Formateur formateur, MultipartFile mf) throws Exception {
        Formateur existingFormateur = fr.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formateur not found with id: " + id));

        existingFormateur.setNom(formateur.getNom());
        existingFormateur.setPrenom(formateur.getPrenom());
        existingFormateur.setEmail(formateur.getEmail());
        existingFormateur.setAdresse(formateur.getAdresse());
        existingFormateur.setTelephone(formateur.getTelephone());

        if (!mf.isEmpty()) {
            String nomFichier = saveImage(mf);
            existingFormateur.setNomImage(nomFichier);
        } else {
            // Retain the existing image name if no new file is uploaded
            existingFormateur.setNomImage(existingFormateur.getNomImage());
        }

        fr.save(existingFormateur);
        return existingFormateur;
    }

    @Override
    public Formateur updateFormateur2(Long id, Formateur formateur) {
        Formateur existingFormateur = fr.findById(id).orElse(null);
        existingFormateur.setNom(formateur.getNom());
        existingFormateur.setPrenom(formateur.getPrenom());
        existingFormateur.setEmail(formateur.getEmail());
        existingFormateur.setAdresse(formateur.getAdresse());
        existingFormateur.setTelephone(formateur.getTelephone());
        existingFormateur.setNomImage(existingFormateur.getNomImage());
        fr.save(existingFormateur);
        return existingFormateur;
    }

    @Override
    public void deleteFormateur(Long id) {
        fr.deleteById(id);

    }

    @Override
    public Formateur getFormateur(Long id) {
        return fr.findById(id).orElse(null);
    }

    @Override
    public List<Formateur> getAllFormateurs() {
        return fr.findAll();
    }

    public String saveImage(MultipartFile mf) throws IOException {
        String nomFile = mf.getOriginalFilename();
        String[] tab=nomFile.split("\\.");
        String newName = tab[0] + System.currentTimeMillis() + "." + tab[1];
        String directory = "src/main/resources/static/photos";
        File fileDir = new File(directory);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        Path path = Paths.get(fileDir.getAbsolutePath(), newName);
        Files.write(path, mf.getBytes());  // Write file to the path
        return newName;

    }

    public byte[] getImage(Long id) throws IOException {
        Formateur formateur = getFormateur(id);
        if (formateur == null || formateur.getNomImage() == null) {
            throw new EntityNotFoundException("Formateur introuvable with id: " + id);
        }
        Path imagePath = Paths.get("src/main/resources/static/photos", formateur.getNomImage());
        if (!Files.exists(imagePath)) {
            throw new EntityNotFoundException("Image Introuvable: " + imagePath);
        }
        return Files.readAllBytes(imagePath);
    }


}
