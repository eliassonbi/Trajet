package org.miage.banqueSohbi.boundary;

import org.miage.banqueSohbi.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;;

public interface CompteResource extends JpaRepository<Compte, String>{
    List<Compte> findByUserid(String userid);

}
