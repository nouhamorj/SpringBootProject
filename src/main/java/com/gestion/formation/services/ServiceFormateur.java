package com.gestion.formation.services;

import com.gestion.formation.entities.Atelier;
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
    public void ajouterFormateur(Formateur formateur, MultipartFile file) throws IOException {
        if(!file.isEmpty())
            formateur.setNomImage(saveImage(file));
        fr.save(formateur);
    }

    @Override
    public void updateFormateur(Long id, Formateur formateur, MultipartFile mf)  throws Exception{
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
        }
        fr.save(existingFormateur);
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

    private String saveImage(MultipartFile mf) throws IOException {
        String nomFile = mf.getOriginalFilename();
        String[] tab=nomFile.split("\\.");
        String newName = tab[0] + System.currentTimeMillis() + "." + tab[1];
        // Use an external directory for saving images
        String directory = "src/main/resources/static/photos";
        File fileDir = new File(directory);

        // Check if the directory exists, if not create it
        if (!fileDir.exists()) {
            fileDir.mkdirs();  // Creates the directory and any necessary parent directories
        }

        Path path = Paths.get(fileDir.getAbsolutePath(), newName);
        Files.write(path, mf.getBytes());  // Write file to the path
        return newName;

    }

    private String uploadImage(MultipartFile mf) throws IOException {
        String oldname = mf.getOriginalFilename();
        String[] tab=oldname.split("\\.");
        String newName = tab[0] + System.currentTimeMillis() + "." + tab[1];

        Path p=Paths.get(System.getProperty("user.home")+"/photosCD2024/", newName);
        Files.write(p,mf.getBytes());
        return newName;
    }
}
