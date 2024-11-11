package org.example.vente.Repistory;

import org.example.vente.Entities.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryVente extends JpaRepository<Vente, Long> {

    // Déclare une méthode personnalisée qui récupère une liste d'objets `Vente` en fonction de l'ID du vendeur (`idSeller`).

    List<Vente> findByIdSeller(Long idSeller);

}
