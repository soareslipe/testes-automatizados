package com.soareslipe.demo.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "planets")
public class Planet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(nullable = false, unique = true)
	private String name;
	@NotEmpty
	private String climate;
	@NotEmpty
	private String terrain;

	public Planet() {
	}

	public Planet(String name, String climate, String terrain) {
		super();
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
	}

	public Planet(Long id, String name, String climate, String terrain) {
		super();
		this.id = id;
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClimate() {
		return climate;
	}

	public void setClimate(String climate) {
		this.climate = climate;
	}

	public String getTerrain() {
		return terrain;
	}

	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}

	@Override
	public int hashCode() {
		return Objects.hash(climate, id, name, terrain);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Planet other = (Planet) obj;
		return Objects.equals(climate, other.climate) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(terrain, other.terrain);
	}

}
