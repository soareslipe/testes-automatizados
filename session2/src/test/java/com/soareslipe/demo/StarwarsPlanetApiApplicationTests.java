package com.soareslipe.demo;



import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import static com.soareslipe.demo.common.PlanetConstants.NEW_PLANET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.soareslipe.demo.domain.Planet;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class StarwarsPlanetApiApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void createPlanetShouldReturnCreated() {
		ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", NEW_PLANET, Planet.class);
		
		Assertions.assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(sut.getBody().getId()).isNotNull();
		Assertions.assertThat(sut.getBody().getName()).isEqualTo(NEW_PLANET.getName());
		Assertions.assertThat(sut.getBody().getClimate()).isEqualTo(NEW_PLANET.getClimate());
		Assertions.assertThat(sut.getBody().getTerrain()).isEqualTo(NEW_PLANET.getTerrain());

	}

}
