package com.soareslipe.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.soareslipe.demo.domain.Planet;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {

	Optional<Planet> findByName(String name);
	
	 @Query("SELECT p FROM Planet p WHERE "
	            + "(:terrain IS NULL OR p.terrain = :terrain) AND "
	            + "(:climate IS NULL OR p.climate = :climate)")
	    List<Planet> getAllPlanets(@Param("terrain") String terrain, @Param("climate") String climate);

}
