package com.gestion.formation.services;

import com.gestion.formation.entities.Formation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IServiceFormation {
    public Page<Formation> rechercherFormationsParTitre(String motCle, Pageable pageable);
    public void ajouterFormation(Formation formation);
    public void updateFormation(Long id, Formation formation, List<Long> ateliersIds);
    void supprimerFormation(Long id);
    public Formation getFormationById(Long id);
}
