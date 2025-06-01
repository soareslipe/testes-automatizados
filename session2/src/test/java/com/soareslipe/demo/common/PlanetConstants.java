package com.soareslipe.demo.common;

import java.util.ArrayList;
import java.util.List;

import com.soareslipe.demo.domain.Planet;

public class PlanetConstants {

	public static final Planet PLANET = new Planet(1L, "name", "climate", "terrain");
	public static final Planet NEW_PLANET = new Planet("name", "climate", "terrain");
	public static final Planet INVALID_PLANET = new Planet("", "", "");
	public static final Planet EMPTY_PLANET = new Planet();
	
	
	public static final Planet TATOOINE = new Planet(1L, "Tatooine", "arid", "desert");
	  public static final Planet ALDERAAN = new Planet(2L, "Alderaan", "temperate", "grasslands, mountains");
	  public static final Planet YAVINIV = new Planet(3L, "Yavin IV", "temperate, tropical", "jungle, rainforests");
	  public static final List<Planet> PLANETS = new ArrayList<Planet>() {
	 
		private static final long serialVersionUID = 1L;

		{
	      add(TATOOINE);
	      add(ALDERAAN);
	      add(YAVINIV);
	    }
	  };
}
