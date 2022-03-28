package org.miage.banqueSohbi.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity     // ORM: mapping des instances de la classe comme nuplet dans H2
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor



public class Compte implements Serializable {
    
    private static final long serialVersionUID = 765432234567L; 
     
    @Id
    
    private String userid;
    private double solde;

}
