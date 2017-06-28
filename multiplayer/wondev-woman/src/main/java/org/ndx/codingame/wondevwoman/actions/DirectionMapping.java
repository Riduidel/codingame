package org.ndx.codingame.wondevwoman.actions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.Geographic;

public class DirectionMapping {
	public static Map<String, Direction> NAMES_TO_DIRECTIONS;

	public static Map<Direction, String> DIRECTIONS_TO_NAMES;

	static {
		final Map<String, Direction> namesToDirections = new TreeMap<>();
		namesToDirections.put(Geographic.NORTH.name.substring(0, 1), Geographic.NORTH);
		namesToDirections.put(Geographic.SOUTH.name.substring(0, 1), Geographic.SOUTH);
		namesToDirections.put(Geographic.EAST.name.substring(0, 1), Geographic.EAST);
		namesToDirections.put(Geographic.WEST.name.substring(0, 1), Geographic.WEST);
		namesToDirections.put("NE", Geographic.NORTHEAST);
		namesToDirections.put("NW", Geographic.NORTHWEST);
		namesToDirections.put("SE", Geographic.SOUTHEAST);
		namesToDirections.put("SW", Geographic.SOUTHWEST);
		NAMES_TO_DIRECTIONS = Collections.unmodifiableMap(namesToDirections);

		final Map<Direction, String> directionsToNames = new HashMap<>();
		for(final Map.Entry<String, Direction> direction : namesToDirections.entrySet()) {
			directionsToNames.put(direction.getValue(), direction.getKey());
		}
		DIRECTIONS_TO_NAMES = Collections.unmodifiableMap(directionsToNames);
	}
}
