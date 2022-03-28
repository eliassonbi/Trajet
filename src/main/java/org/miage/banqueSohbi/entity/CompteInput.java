package org.miage.banqueSohbi.entity;

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
public class CompteInput {


    @Pattern(regexp = "[0-9]+") 
    private double solde;

	
}
