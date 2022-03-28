package org.miage.banqueSohbi;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.miage.banqueSohbi.boundary.CompteResource;
import org.miage.banqueSohbi.entity.Compte;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class compteSohbiApplication {

	public static void main(String[] args) {
		SpringApplication.run(compteSohbiApplication.class, args);
	}

	@Bean
	public OpenAPI TrajetAPI() {
		return new OpenAPI().info(new Info()
			.title("Trajet API")
			.version("1.0")
			.description("Documentation sommaire de API Train 1.0"));
	}
	@Bean
	CommandLineRunner run(CompteResource c){
		return args -> {
			c.save(new Compte("32",2872.9 ));
			c.save(new Compte("26", 30.2));
			c.save(new Compte("938", 1.0));
			c.save(new Compte("928",129.8 ));
			c.save(new Compte("1",76.9 ));
	

			};
		} 
	}