package org.miage.trainSohbi;

import static io.restassured.RestAssured.when;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
import org.miage.trainSohbi.boundary.TrajetResource;
import org.miage.trainSohbi.entity.Trajet;
import org.miage.trainSohbi.entity.TrajetInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.apache.http.HttpStatus;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.not;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TrajetServiceApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	TrajetResource ir;

	@BeforeEach
	public void setupContext() {
		ir.deleteAll();
		RestAssured.port = port;
	}

	@Test
	public void pingApi() {
		when().get("/Trajets").then().statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void getOne() {
		Trajet i1 = new Trajet(UUID.randomUUID().toString(), "Florange", "Hagondange","12/10/1999 10:00","12/10/1999 16:20", 4.0, "fenetre", false);
		ir.save(i1);
		Response response = when().get("/Trajets/"+i1.getId())
								.then()
								.statusCode(HttpStatus.SC_OK)
								.extract()
								.response();
		String jsonAsString = response.asString();
		assertThat(jsonAsString,containsString("Florange"));
	}

	@Test
	public void getAll() {
		Trajet i1 = new Trajet(UUID.randomUUID().toString(), "Florange", "Saint-Baussant","12/10/2022 10:00","12/10/2022 16:20", 7.0, "couloir", false);
		ir.save(i1);
		Trajet i2 = new Trajet(UUID.randomUUID().toString(), "Lérouville", "Strasbourg","12/10/2022 10:00","01/10/2022 09:20", 11.0, "cote", false);
		ir.save(i2);
		when().get("/Trajets/")
								.then()
								.statusCode(HttpStatus.SC_OK)
								.and()
								.assertThat()
								.body("size()",equalTo(2));
	}

	@Test
	public void getNotFound() {
		when().get("/Trajets/0").then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void retourAPI() throws Exception{
		Trajet i1 = new Trajet(UUID.randomUUID().toString(),"Nancy", "Metz","12/10/2022 10:00","12/10/2022 14:00",10.9,"couloir", false);
		Trajet i2 = new Trajet(UUID.randomUUID().toString(),"Metz", "Nancy","12/10/2022 19:00","12/10/2022 23:00",10.9,"fenetre", false);
		ir.save(i1);
		ir.save(i2);

		Response response = when().get("/Trajets/"+i1.getId()+"/retour")
								.then()
								.statusCode(HttpStatus.SC_OK)
								.extract()
								.response();
		String jsonAsString = response.asString();
		assertThat(jsonAsString,containsString(i2.getDatedepart()));
	}

	@Test
	public void nonRetourAPI() throws Exception{
		Trajet i1 = new Trajet(UUID.randomUUID().toString(),"Nancy", "Metz","12/10/2022 10:00","12/10/2022 14:00",10.9,"couloir", false);
		Trajet i2 = new Trajet(UUID.randomUUID().toString(),"Metz", "Nancy","12/10/2022 13:59","13/10/2022 23:00",10.9,"fenetre", false);
		ir.save(i1);
		ir.save(i2);

		Response response = when().get("/Trajets/"+i1.getId()+"/retour")
								.then()
								.statusCode(HttpStatus.SC_OK)
								.extract()
								.response();
		String jsonAsString = response.asString();
		assertThat(jsonAsString, not(containsString(i2.getDatedepart())));
	}

	@Test
	public void postApi() throws Exception{
		TrajetInput i1 = new TrajetInput("12/10/2022 10:00","12/10/2022 13:00","Nancy", "Metz",10.9,"couloir", false);
		Response response = given()
							.body(this.toJsonString(i1))
							.contentType(ContentType.JSON)
							.when()
							.post("/Trajets")
							.then()
							.statusCode(HttpStatus.SC_CREATED)
							.extract()
							.response();
		String location = response.getHeader("Location");
		when().get(location).then().statusCode(HttpStatus.SC_OK);


		
	}

	private String toJsonString(Object o) throws Exception {
		ObjectMapper map = new ObjectMapper();
		return map.writeValueAsString(o);
	}








}
