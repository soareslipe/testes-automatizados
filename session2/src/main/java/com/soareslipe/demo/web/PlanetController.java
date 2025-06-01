package com.soareslipe.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soareslipe.demo.domain.Planet;
import com.soareslipe.demo.services.PlanetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/planets")
public class PlanetController {
	
	@Autowired
	private PlanetService planetService;;

	@PostMapping
	public ResponseEntity<Planet> create(@RequestBody @Valid Planet planet){
		Planet planetCreated = planetService.create(planet);
		return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Planet> getPlanetById(@PathVariable("id") Long id){
		return planetService.getPlanetById(id).map(planet -> ResponseEntity.ok(planet))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@GetMapping("name/{name}")
	public ResponseEntity<Planet> getPlanetByName(@PathVariable("name") String name){
		return planetService.getPlanetByName(name).map(planet -> ResponseEntity.ok(planet))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@GetMapping
	public ResponseEntity<List<Planet>> getPlanets(@RequestParam(required = false) String terrain,
			@RequestParam(required = false) String climate){
		List<Planet> planets = planetService.getPlanets(terrain, climate);
		return ResponseEntity.ok(planets);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Planet> deletePlanet(@PathVariable("id") Long id){
		planetService.deletePlanet(id);
		return ResponseEntity.noContent().build();
	}
}
