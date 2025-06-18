package com.soareslipe.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import static com.soareslipe.demo.common.PlanetConstants.NEW_PLANET;
import static com.soareslipe.demo.common.PlanetConstants.TATOOINE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.soareslipe.demo.domain.Planet;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "/import.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "/remove_planets.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
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

	@Test
	public void getPlanetByIdShouldReturnPlanet() {
		ResponseEntity<Planet> res = restTemplate.getForEntity("/planets/1", Planet.class);

		Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(res.getBody()).isEqualTo(TATOOINE);
	}

	@Test
	public void getPlanetByNameShouldReturnPlanet() {
		ResponseEntity<Planet> res = restTemplate.getForEntity("/planets/name/" + TATOOINE.getName(), Planet.class);
		

		Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(res.getBody()).isEqualTo(TATOOINE);
	}
	
	
	@Test
	public void getPlanetsShouldReturnAllPlanets() {
		ResponseEntity<Planet[]> res = restTemplate.getForEntity("/planets", Planet[].class);
	
		Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(res.getBody()).hasSize(3);
		Assertions.assertThat(res.getBody()[0]).isEqualTo(TATOOINE);
	}
	
	@Test
	public void getPlanetsByClimateShouldReturnPlanets() {
		ResponseEntity<Planet[]> res = restTemplate.getForEntity("/planets?climate=" + TATOOINE.getClimate(), Planet[].class);
		
		Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(res.getBody()).hasSize(1);
		Assertions.assertThat(res.getBody()[0]).isEqualTo(TATOOINE);
	}
	
	@Test
	public void getPlanetsByTerrainShouldReturnPlanets() {
		ResponseEntity<Planet[]> res = restTemplate.getForEntity("/planets?terrain=" + TATOOINE.getTerrain(), Planet[].class);
		
		Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(res.getBody()).hasSize(1);
		Assertions.assertThat(res.getBody()[0]).isEqualTo(TATOOINE);
	}
	
	@Test
	public void removePlanetsShouldReturnNoContent() {
		ResponseEntity<Void> res = restTemplate.exchange("/planets/1", HttpMethod.DELETE, null, Void.class);
		
		Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}
