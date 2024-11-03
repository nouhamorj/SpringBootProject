package com.gestion.formation.repositories;

import com.gestion.formation.entities.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Page<Theme> findByNomContains(String motCle, Pageable pageable);
}
