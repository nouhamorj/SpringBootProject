package com.gestion.formation.repositories;

import com.gestion.formation.entities.Formateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {
    Page<Formateur> findByNomContains(String motCle, Pageable pageable);
}
