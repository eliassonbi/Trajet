package org.miage.trainSohbi.boundary;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.miage.trainSohbi.entity.Trajet;

public interface TrajetResource extends JpaRepository<Trajet, String>{
    List<Trajet> findByLieudepartAndLieuarriveeAndDatedepart(String lieuDepart, String lieuArrivee, String date);
    List<Trajet> findByLieudepartAndLieuarriveeAndCote(String lieuDepart, String lieuArrivee, String cote);
    List<Trajet> findByLieudepartAndLieuarrivee(String lieuDepart, String lieuArrivee);

}
