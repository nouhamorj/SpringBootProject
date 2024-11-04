package com.gestion.formation.services;

import com.gestion.formation.entities.Formateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IServiceFormateur {
    Page<Formateur> rechercherFormateursParNom(String motCle, Pageable p);
    public Formateur ajouterFormateur(Formateur formateur, MultipartFile mf) throws Exception;
    public Formateur updateFormateur(Long id, Formateur formateur, MultipartFile mf) throws Exception;
    public void deleteFormateur(Long id);
    public Formateur getFormateur(Long id);
    public List<Formateur> getAllFormateurs();
}