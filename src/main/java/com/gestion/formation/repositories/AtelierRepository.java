package com.gestion.formation.repositories;

import com.gestion.formation.entities.Atelier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AtelierRepository extends JpaRepository<Atelier, Long> {
    // Recherche par titre d'atelier
    Page<Atelier> findByTitreContains(String mc, Pageable pageable);
    // Recherche par nom du thème lié (via une requête JPQL)
    @Query("SELECT a FROM Atelier a JOIN a.themes t WHERE t.nom LIKE %:mc%")
    List<Atelier> findByThemeNomContains(@Param("mc") String mc);

}
