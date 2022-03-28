package org.miage.banqueSohbi;

import static io.restassured.RestAssured.when;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
import org.miage.banqueSohbi.boundary.CompteResource;
import org.miage.banqueSohbi.entity.Compte;
import org.miage.banqueSohbi.entity.CompteInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.apache.http.HttpStatus;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CompteServiceApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	CompteResource ir;

	@BeforeEach
	public void setupContext() {
		ir.deleteAll();
		RestAssured.port = port;
	}

	@Test
	public void pingApi() {
		when().get("/Banque").then().statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void getOne() {
		Compte i1 = new Compte(UUID.randomUUID().toString(),1000.0);
		ir.save(i1);
		Response response = when().get("/Banque/"+i1.getUserid())
								.then()
								.statusCode(HttpStatus.SC_OK)
								.extract()
								.response();
		String jsonAsString = response.asString();
		assertThat(jsonAsString,containsString("1000.0"));
	}

	@Test
	public void getAll() {
		Compte i1 = new Compte(UUID.randomUUID().toString(),736.9);
		ir.save(i1);
		Compte i2 = new Compte(UUID.randomUUID().toString(),76209.9);
		ir.save(i2);
		when().get("/Banque/")
								.then()
								.statusCode(HttpStatus.SC_OK)
								.and()
								.assertThat()
								.body("size()",equalTo(2));
	}

	@Test
	public void getNotFound() {
		when().get("/Banque/1873").then().statusCode(HttpStatus.SC_NOT_FOUND);
	}


	@Test
    public void testPayementFaux() throws Exception {
        Compte i1 = new Compte(UUID.randomUUID().toString(),10.0);
		ir.save(i1);
		Response response = when().get("/Banque/"+i1.getUserid()+"/50")
								.then()
								.statusCode(HttpStatus.SC_OK)
								.extract()
								.response();
		String jsonAsString = response.asString();
		assertThat(jsonAsString,containsString("false"));
    }

	@Test
    public void testPayementBon() throws Exception {
        Compte i1 = new Compte(UUID.randomUUID().toString(),100.0);
		ir.save(i1);
		Response response = when().get("/Banque/"+i1.getUserid()+"/50")
								.then()
								.statusCode(HttpStatus.SC_OK)
								.extract()
								.response();
		String jsonAsString = response.asString();
		assertThat(jsonAsString,containsString("true"));
    }








}
