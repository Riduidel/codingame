package org.ndx.codingame.wondevwoman.actions;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Geographic;

public class Dual implements ConstructableInUnitTest, Action {
	public static Map<String, Direction> DIRECTIONS;

	static {
		final Map<String, Direction> d = new TreeMap<>();
		d.put(Geographic.NORTH.name.substring(0, 1), Geographic.NORTH);
		d.put(Geographic.SOUTH.name.substring(0, 1), Geographic.SOUTH);
		d.put(Geographic.EAST.name.substring(0, 1), Geographic.EAST);
		d.put(Geographic.WEST.name.substring(0, 1), Geographic.WEST);
		d.put("NE", Geographic.NORTHEAST);
		d.put("NW", Geographic.NORTHWEST);
		d.put("SE", Geographic.SOUTHEAST);
		d.put("SW", Geographic.SOUTHWEST);
		DIRECTIONS = Collections.unmodifiableMap(d);
	}
	private static final String CLASS_NAME = Dual.class.getSimpleName();

	public final Collection<String> actions;
	public final int playerIndex;
	public final String moveKey;
	public final String buildKey;

	public Dual(final String atype, final int index, final String dir1, final String dir2) {
		actions = Arrays.asList(atype.split("&"));
		playerIndex = index;
		moveKey = dir1;
		buildKey = dir2;
	}

	@Override
	public String toCommandString() {
		return String.format("%s %d %s %s", actionsToString(), playerIndex, moveKey, buildKey);
	}

	private String actionsToString() {
		return actions.stream().collect(Collectors.joining("&"));
	}

	@Override
	public String toString() {
		return toCommandString();
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		return new StringBuilder("new ").append(CLASS_NAME).append("(")
				.append("\"").append(actionsToString()).append("\", ")
				.append(playerIndex).append(", ")
				.append("\"").append(moveKey).append("\"").append(", ")
				.append("\"").append(buildKey).append("\"")
				.append(")")
				;
	}

	public DiscretePoint move() {
		return DIRECTIONS.get(moveKey);
	}

	public DiscretePoint build() {
		return DIRECTIONS.get(buildKey);
	}
}
