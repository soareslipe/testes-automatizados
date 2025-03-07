package com.soareslipe.demo.common;

import com.soareslipe.demo.domain.Planet;

public class PlanetConstants {

	public static final Planet PLANET = new Planet(1L, "name", "climate", "terrain");
	public static final Planet INVALID_PLANET = new Planet(1L, "", "", "");
}
