package com.gestion.formation.services;

import com.gestion.formation.entities.Atelier;
import com.gestion.formation.entities.Formation;
import com.gestion.formation.repositories.FormationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Service
public class ServiceFormation implements IServiceFormation {
    private FormationRepository fr;
    private IServiceAtelier    serviceAtelier;
    @Override
    public Page<Formation> rechercherFormationsParTitre(String motCle, Pageable pageable) {
        return fr.findByLibelleContains(motCle,pageable);
    }

    @Override
    public void ajouterFormation(Formation formation) {
        fr.save(formation);
    }

    @Override
    public void updateFormation(Long id, Formation formation, List<Long> ateliersIds) {
        // Récupérer la formation existante
        Formation existingFormation = fr.findById(id).orElse(null);
        if (existingFormation != null) {
            // Mettre à jour les champs de base
            existingFormation.setLibelle(formation.getLibelle());
            existingFormation.setDescription(formation.getDescription());
            existingFormation.setDateFormation(formation.getDateFormation());

            // Vider la liste actuelle des ateliers pour éviter les doublons ou conserver les anciens ateliers
            existingFormation.getAteliers().clear();

            // Ajouter la nouvelle liste des ateliers sélectionnés
            if (ateliersIds != null) {
                for (Long atelierId : ateliersIds) {
                    Atelier atelier = serviceAtelier.getAtelier(atelierId);
                    if (atelier != null) {
                        existingFormation.getAteliers().add(atelier);
                    }
                }
            }

            // Sauvegarder les modifications dans le repository
            fr.save(existingFormation);
        }
    }


    @Override
    public void supprimerFormation(Long id) {
        fr.deleteById(id);

    }

    @Override
    public Formation getFormationById(Long id) {
        return fr.findById(id).orElse(null);
    }
}
