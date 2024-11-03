package com.gestion.formation.services;

import com.gestion.formation.entities.Atelier;
import com.gestion.formation.entities.DocumentPDF;
import com.gestion.formation.repositories.DocumentPDFRepository;
import lombok.AllArgsConstructor;
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
public class ServiceDocumentPDF implements IServiceDocumentPDF{
    private DocumentPDFRepository documentPDFRepository;
    @Override
    public void save(DocumentPDF documentPDF) {
        documentPDFRepository.save(documentPDF);
    }

    @Override
    public void deleteById(Long id) {
        documentPDFRepository.deleteById(id);

    }

    @Override
    public List<DocumentPDF> findAll() {
        return documentPDFRepository.findAll();
    }

    @Override
    public DocumentPDF findById(Long id) {
        return documentPDFRepository.findById(id).get();
    }

    public List<DocumentPDF> findByAtelierId(Long atelierId) {
        // Logique pour trouver des documents liés à un atelier spécifique
        return documentPDFRepository.findAll().stream() //convertir la liste en un flux(stream)
                .filter(doc -> doc.getAtelier() != null && doc.getAtelier().getId().equals(atelierId))
                .toList();
    }



    public DocumentPDF uploadPDF(MultipartFile pdfFile, Atelier atelier) throws IOException {
        // Vérifiez que le fichier est un PDF
        if (!pdfFile.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("Le fichier doit être un PDF.");
        }

        // Obtenez le nom original du fichier
        String nomFile = pdfFile.getOriginalFilename();
        String[] tab = nomFile.split("\\.");
        String newName = tab[0] + System.currentTimeMillis() + "." + tab[1]; // Nouveau nom avec timestamp

        // Utilisez un répertoire externe pour enregistrer les PDF
        String directory = "src/main/resources/static/pdfs"; // Spécifiez le répertoire où vous voulez enregistrer les PDF
        File fileDir = new File(directory);

        // Vérifiez si le répertoire existe, sinon créez-le
        if (!fileDir.exists()) {
            fileDir.mkdirs(); // Crée le répertoire et tous les répertoires parents nécessaires
        }

        // Définissez le chemin où le fichier sera enregistré
        Path path = Paths.get(fileDir.getAbsolutePath(), newName);
        Files.write(path, pdfFile.getBytes()); // Écrivez le fichier au chemin

        // Créez un nouvel objet DocumentPDF
        DocumentPDF documentPDF = new DocumentPDF();
        documentPDF.setNom(nomFile); // Nom d'origine
        documentPDF.setChemin(path.toString()); // Chemin du fichier enregistré
        documentPDF.setAtelier(atelier); // Référence à l'atelier

        // Enregistrez le document dans la base de données
        return documentPDFRepository.save(documentPDF);
    }
}
