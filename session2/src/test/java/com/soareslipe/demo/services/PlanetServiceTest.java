package com.soareslipe.demo.services;

import static com.soareslipe.demo.common.PlanetConstants.INVALID_PLANET;
import static com.soareslipe.demo.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soareslipe.demo.domain.Planet;
import com.soareslipe.demo.repositories.PlanetRepository;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
	
	@InjectMocks
	private PlanetService planetService;
	
	@Mock
	private PlanetRepository planetRepository;

	@Test
	public void createPlanetShouldReturnPlanetWhenValidData() {
	when(planetRepository.save(PLANET)).thenReturn(PLANET);
		
	Planet sut = planetService.create(PLANET);
	Assertions
		.assertThat(sut).isEqualTo(PLANET);
	}
	
	@Test
	public void createPlanetShouldThrowsExceptionWhenInvalidData() {
		when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
		Assertions.assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
	}
	
	@Test
	public void getPlanetByIdShouldReturnPlanetWhenIdExists() {
	when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));
	
	Optional<Planet> sut = planetService.getPlanetById(1L);
	
	Assertions.assertThat(sut).isNotEmpty();
	Assertions.assertThat(sut.get()).isEqualTo(PLANET);
	
	}
	
	@Test
	public void getPlanetByIdShouldThrowsNotFoundExceptionWhenIdDoesNotExists() {
		when(planetRepository.findById(1L)).thenReturn(Optional.empty());
		
		Optional<Planet> sut = planetService.getPlanetById(1L);
		
		Assertions.assertThat(sut).isEmpty();
	}
	
	@Test
	public void getPlanetByNameShouldThrowsNotFoundExceptionWhenNameDoesNotExists() {
		when(planetRepository.findByName("name")).thenReturn(Optional.empty());
		
		Optional<Planet> sut = planetService.getPlanetByName("name");
		
		Assertions.assertThat(sut).isEmpty();
	}
	
	
	@Test
	public void getPlanetByNameShouldReturnPlanetWhenNameExists() {
	when(planetRepository.findByName("name")).thenReturn(Optional.of(PLANET));
	
	Optional<Planet> sut = planetService.getPlanetByName("name");
	
	Assertions.assertThat(sut).isNotEmpty();
	Assertions.assertThat(sut.get()).isEqualTo(PLANET);
	
	}
	
	@Test
	public void getAllPlanetsShouldReturnPlanetsWhenExists() {
	List<Planet> planets = new ArrayList<>();
	planets.add(PLANET);
	
	when(planetRepository.getAllPlanets(PLANET.getTerrain(), PLANET.getClimate())).thenReturn(planets);
	
	List<Planet> sut = planetRepository.getAllPlanets(PLANET.getTerrain(), PLANET.getClimate());
	Assertions.assertThat(sut).isNotEmpty();
	Assertions.assertThat(sut).hasSize(1);
	Assertions.assertThat(sut.get(0)).isEqualTo(PLANET);
	}
	
	@Test
	public void getAllPlanetsShouldReturnNothingWhenPlanetsDoesNotExists() {
	when(planetRepository.getAllPlanets(any(), any())).thenReturn(Collections.emptyList());
	
	List<Planet> sut = planetRepository.getAllPlanets(PLANET.getTerrain(), PLANET.getClimate());
	Assertions.assertThat(sut).isEmpty();
		
	}
	
	@Test
	public void deletePlanetShouldReturnNoContentWhenIdExists() {
		assertThatCode(() -> planetService.deletePlanet(1L)).doesNotThrowAnyExceptionExcept();
	}
	
	@Test
	public void deletePlanetShouldThrowsNotFoundExceptionWhenIdDoesNotExists() {
		doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);
		
		assertThatThrownBy(() -> planetService.deletePlanet(99L)).isInstanceOf(RuntimeException.class);
	}
}
