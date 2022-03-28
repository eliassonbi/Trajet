package org.miage.trainSohbi;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.miage.trainSohbi.boundary.TrajetResource;
import org.miage.trainSohbi.entity.Trajet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class trainSohbiApplication {

	public static void main(String[] args) {
		SpringApplication.run(trainSohbiApplication.class, args);
	}

	@Bean
	public OpenAPI TrajetAPI() {
		return new OpenAPI().info(new Info()
			.title("Trajet API")
			.version("1.0")
			.description("Documentation sommaire de API Train 1.0"));
	}

	@Configuration
	public class SpringFoxConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
   	 	}
	}
	@Bean
	CommandLineRunner run(TrajetResource Tr){
		return args -> {
			Tr.save(new Trajet("6543", "Paris", "Marseille","12/05/2022 12:00","12/05/2022 16:20", 50.0, "fenetre", false));
			Tr.save(new Trajet("98", "Thionville", "Paris", "12/05/2022 15:00","12/05/2022 20:39", 80.5, "couloir", false));
			Tr.save(new Trajet("876", "Toulouse", "Metz", "13/05/2022 10:00","13/05/2022 18:42", 57.8, "fenetre", true));
			Tr.save(new Trajet("6", "Paris", "Bordeaux", "13/05/2022 23:15","14/05/2022 04:06", 98.2, "couloir", false));
			Tr.save(new Trajet("1", "Nice", "Paris", "12/05/2022 19:53","13/05/2022 01:05", 64.3, "couloir", true));
			Tr.save(new Trajet("32", "Paris", "Thionville", "12/05/2022 23:58","13/05/2022 04:04", 80.0, "fenetre", false));
			Tr.save(new Trajet("30", "Paris", "Thionville", "12/05/2022 22:38","13/05/2022 02:10", 70.0, "fenetre", false));
			Tr.save(new Trajet("90", "Nice", "Marseille", "01//05/2022 11:00","02/05/2022 13:00", 100.0, "couloir", false));
			Tr.save(new Trajet("100", "Lérouville", "Luxembourg", "01/03/2022 20:53","01/03/2022 23:05", 50.3, "fenetre", false));
			};
		} 
	}