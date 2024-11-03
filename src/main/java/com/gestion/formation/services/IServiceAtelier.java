package com.gestion.formation.services;

import com.gestion.formation.entities.Atelier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IServiceAtelier {
    Page <Atelier> rechercherAteliersParTitre(String motCle, Pageable p);
    public List<Atelier> rechercherAteliersParTheme(String motCle);
    public List<Atelier> getAteliers();
    public void ajouterAtelier(Atelier atelier, MultipartFile[] pdfFiles) throws IOException;
    public void updateAtelier(Long id, Atelier atelier);
    public void deleteAtelier(Long id);
    public Atelier getAtelier(Long id);
}
