package com.soareslipe.demo.repositories;

import static com.soareslipe.demo.common.PlanetConstants.EMPTY_PLANET;
import static com.soareslipe.demo.common.PlanetConstants.INVALID_PLANET;
import static com.soareslipe.demo.common.PlanetConstants.NEW_PLANET;
import static com.soareslipe.demo.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import com.soareslipe.demo.domain.Planet;

@DataJpaTest
public class PlanetRepositoryTest {
	
	@Autowired
	private PlanetRepository planetRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@AfterEach
	public void afterEach() {
		NEW_PLANET.setId(null);
	}
	
	@Test
	public void createPlanetShouldReturnPlanetWhenValidData() {
		Planet planet = planetRepository.save(NEW_PLANET);
		
		Planet sut = testEntityManager.find(Planet.class, planet.getId());
		
		Assertions.assertThat(sut).isNotNull();
		Assertions.assertThat(sut.getClimate()).isEqualTo(NEW_PLANET.getClimate());
		Assertions.assertThat(sut.getName()).isEqualTo(NEW_PLANET.getName());
		Assertions.assertThat(sut.getTerrain()).isEqualTo(NEW_PLANET.getTerrain());
	}
	
	@Test
	public void createPlanetShouldThrowsExceptionWhenInvalidData() {
		assertThatThrownBy(() -> planetRepository.save(EMPTY_PLANET)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> planetRepository.save(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
	}
	
	@Test
	public void createPlanetShouldThrowsExceptionWhenPlanetAlreadyExists() {
		Planet planet = testEntityManager.persistAndFlush(NEW_PLANET);
		testEntityManager.detach(planet);
		planet.setId(null);
		
		assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
		
	}
	
	@Test
	public void getPlanetByIdShouldReturnPlanetWhenIdExists() {
		Planet planet = testEntityManager.persistAndFlush(NEW_PLANET);
		Optional<Planet> sut = planetRepository.findById(planet.getId()); 
		
		Assertions.assertThat(sut).isNotEmpty();
		Assertions.assertThat(sut.get()).isEqualTo(planet);
		
	}
	
	@Test
	public void getPlanetByIdShouldReturnEmptyWhenIdDoesNotExists() {
		Optional<Planet> sut = planetRepository.findById(1L); 
		
		Assertions.assertThat(sut).isEmpty();
	}
	
	@Test
	public void getPlanetByNameShouldReturnPlanetWhenNameExists() {
		Planet planet = testEntityManager.persistAndFlush(NEW_PLANET);
		Optional<Planet> sut = planetRepository.findByName(planet.getName()); 
		
		Assertions.assertThat(sut).isNotEmpty();
		Assertions.assertThat(sut.get()).isEqualTo(planet);
		
	}
	
	@Test
	public void getPlanetByNameShouldReturnEmptyWhenNameDoesNotExists() {
		Optional<Planet> sut = planetRepository.findByName(NEW_PLANET.getName()); 
		
		Assertions.assertThat(sut).isEmpty();
	}
	
	@Sql(scripts = "/import.sql")
	@Test
	public void listPlanetsShouldReturnsFilteredPlanets() {
		List<Planet> responseWithoutFilters = planetRepository.getAllPlanets(null, null);
		List<Planet> responseWithFilters = planetRepository.getAllPlanets(TATOOINE.getTerrain(), TATOOINE.getClimate() );
		
		
		assertThat(responseWithoutFilters).isNotEmpty();
		assertThat(responseWithoutFilters).hasSize(3);
		assertThat(responseWithFilters).isNotEmpty();
		assertThat(responseWithFilters).hasSize(1);
		assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);

	}
	
	@Test
	public void listPlanetsShouldReturnsNoRetults() {
		List<Planet> response = planetRepository.getAllPlanets(null, null);
		
		assertThat(response).isEmpty();
	}
	
	@Test
	public void removePlanetShouldRemovePlanetFromDatabaseWhenIdExists() {
		Planet planet = testEntityManager.persistAndFlush(NEW_PLANET);
		
		planetRepository.deleteById(planet.getId());
		Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
		
		assertThat(removedPlanet).isNull();
		
	}
	
	@Test
	public void removePlanetShouldDoNothingWhenIdDoesNotExist() {
		assertThatCode(() -> planetRepository.deleteById(999L)).doesNotThrowAnyException();
	}

}
