package org.ndx.codingame.code4life.entities;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public enum Module {
	START_POS(),
	SAMPLES(to(START_POS, 2)),
	DIAGNOSIS(to(START_POS, 2), to(SAMPLES, 3)),
	MOLECULES(to(START_POS, 2), to(SAMPLES, 3), to(DIAGNOSIS, 3)),
	LABORATORY(to(START_POS, 2), to(SAMPLES, 3), to(DIAGNOSIS, 4), to(MOLECULES, 3));

	private static Map.Entry<Module, Integer> to(final Module other, final int distance) {
		return new AbstractMap.SimpleEntry(other, distance);
	}

	private Map<String, Integer> distances;

	private Module(final Map.Entry<Module, Integer>... inputDistances) {
		distances = new HashMap<>();
		for (final Map.Entry<Module, Integer> d : inputDistances) {
			distances.put(d.getKey().name(), d.getValue());
		}
	}

	/**
	 * Distance (in turns) between two modules
	 *
	 * @param other
	 * @return
	 */
	public int distanceTo(final Module other) {
		if (distances.containsKey(other.name())) {
			return distances.get(other.name());
		} else if (other.distances.containsKey(name())) {
			return other.distanceTo(this);
		} else {
			return 0;
		}
	}
}
