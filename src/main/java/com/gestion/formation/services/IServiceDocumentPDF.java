package com.gestion.formation.services;

import com.gestion.formation.entities.DocumentPDF;

import java.util.List;

public interface IServiceDocumentPDF {
    void save(DocumentPDF documentPDF); // Enregistrer un document PDF
    void deleteById(Long id); // Supprimer un document PDF par ID
    List<DocumentPDF> findAll(); // Trouver tous les documents PDF
    DocumentPDF findById(Long id); // Trouver un document PDF par ID
    List<DocumentPDF> findByAtelierId(Long atelierId);
}
