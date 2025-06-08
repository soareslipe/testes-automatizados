package com.soareslipe.demo.services.web;

import static com.soareslipe.demo.common.PlanetConstants.EMPTY_PLANET;
import static com.soareslipe.demo.common.PlanetConstants.INVALID_PLANET;
import static com.soareslipe.demo.common.PlanetConstants.PLANET;
import static com.soareslipe.demo.common.PlanetConstants.PLANETS;
import static com.soareslipe.demo.common.PlanetConstants.TATOOINE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soareslipe.demo.services.PlanetService;
import com.soareslipe.demo.web.PlanetController;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private PlanetService planetService;
	

	@Test
	public void createPlanetShouldReturnCreatedWhenValidData() throws Exception{
		when(planetService.create(PLANET)).thenReturn(PLANET);
		
		String jsonBody = objectMapper.writeValueAsString(PLANET);
		
		mockMvc.perform(post("/planets").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$").value(PLANET));
	}
	
	@Test
	public void createPlanetShouldReturnBadRequestWhenInvalidData() throws Exception {
		String jsonBodyInvalid = objectMapper.writeValueAsString(INVALID_PLANET);
		String jsonBodyEmpty = objectMapper.writeValueAsString(EMPTY_PLANET);
		
		
		mockMvc.perform(post("/planets").content(jsonBodyInvalid).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity());
		
		mockMvc.perform(post("/planets").content(jsonBodyEmpty).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void createPlanetShouldReturnBadRequestWhenPlanetExists() throws Exception {
		when(planetService.create(PLANET)).thenThrow(DataIntegrityViolationException.class);
		
		String jsonBody = objectMapper.writeValueAsString(PLANET);
		
		mockMvc.perform(post("/planets").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isConflict());
	}
	
	@Test
	public void getPlanetByIdShouldReturnPlanetWhenIdExists() throws Exception {
		when(planetService.getPlanetById(1L)).thenReturn(Optional.of(PLANET));
		
		
		mockMvc.perform(get("/planets/1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").value(PLANET));
	}
	
	@Test
	public void getPlanetByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		mockMvc.perform(get("/planets/1"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void getPlanetByNameShouldReturnPlanetWhenNameExists() throws Exception {
		when(planetService.getPlanetByName(PLANET.getName())).thenReturn(Optional.of(PLANET));
		
		
		mockMvc.perform(get("/planets/name/" + PLANET.getName()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").value(PLANET));
	}
	
	@Test
	public void getPlanetByNameShouldReturnNotFoundWhenNameDoesNotExists() throws Exception {
		mockMvc.perform(get("/planets/name/" + PLANET.getName()))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void listPlanetsShouldReturnsFilteredPlanets() throws Exception {
		when(planetService.getPlanets(null, null)).thenReturn(PLANETS);
		when(planetService.getPlanets(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));
		
		mockMvc.perform(get("/planets"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(3)));
		
		mockMvc.perform(get("/planets?" 
		+ "terrain=" + TATOOINE.getTerrain()
		+ "&climate=" + TATOOINE.getClimate()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0]").value(TATOOINE));
	}
	
	@Test
	public void listPlanetsShouldReturnsNoRetults() throws Exception {
		when(planetService.getPlanets(null, null)).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get("/planets"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(0)));
	}
	
	@Test
	public void removePlanetShouldReturnNoContentWhenIdExists() throws Exception {
		mockMvc.perform(delete("/planets/1"))
		.andExpect(status().isNoContent());
	}
	
	@Test
	public void removePlanetShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		final Long planetId = 1L;
		
		doThrow(new EmptyResultDataAccessException(1)).when(planetService).deletePlanet(planetId);
		
		mockMvc.perform(delete("/planets/" + planetId))
		.andExpect(status().isNotFound());
	}
}
