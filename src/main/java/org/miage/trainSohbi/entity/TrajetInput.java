package org.miage.trainSohbi.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrajetInput {


    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private String datedepart;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private String datearrivee;
    @Size(min=3)
    private String lieudepart;
    @Size(min=3)
    private String lieuarrivee;
    private double prix;
    @Size(min=4)
    private String cote;
    private Boolean reserve;

	
}




