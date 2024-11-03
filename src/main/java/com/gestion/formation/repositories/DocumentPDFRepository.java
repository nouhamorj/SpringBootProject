package com.gestion.formation.repositories;

import com.gestion.formation.entities.DocumentPDF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentPDFRepository extends JpaRepository<DocumentPDF, Long> {
}
