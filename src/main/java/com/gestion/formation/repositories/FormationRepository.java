package com.gestion.formation.repositories;

import com.gestion.formation.entities.Formation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    Page<Formation> findByLibelleContains(String motCle, Pageable pageable);
}
