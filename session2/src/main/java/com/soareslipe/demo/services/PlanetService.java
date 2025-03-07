package com.soareslipe.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.soareslipe.demo.domain.Planet;
import com.soareslipe.demo.repositories.PlanetRepository;

@Service
public class PlanetService {
	
	
	private PlanetRepository planetRepository;
	
	public PlanetService(PlanetRepository planetRepository) {
		this.planetRepository = planetRepository;
	}
	
	public Planet create(Planet planet) {
		return planetRepository.save(planet);
	}

	public Optional<Planet> getPlanetById(Long id) {
		return planetRepository.findById(id);
	}

	public Optional<Planet> getPlanetByName(String name) {
		return planetRepository.findByName(name);
	}

	public List<Planet> getPlanets(String terrain, String climate) {
		return planetRepository.getAllPlanets(terrain, climate);
	}

	public void deletePlanet(Long id) {
		planetRepository.deleteById(id);
	}

}
