package org.ndx.codingame.lib2d.discrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Geographic {
	public static final Direction NORTH = new Direction(0, -1, "NORTH");
	public static final Direction SOUTH = new Direction(0, 1, "SOUTH");
	public static final Direction EAST = new Direction(1, 0, "EAST");
	public static final Direction WEST = new Direction(-1, 0, "WEST");
	public static final List<Direction> DIRECTIONS = Collections.unmodifiableList(Arrays.asList(
			NORTH,
			SOUTH,
			EAST,
			WEST
			));

	public static final List<Direction> DIRECTIONS_AND_DIAGONALS;

	public static final List<Direction> DIRECTIONS_AND_STAY;

	static {
		List<Direction> toUse = new ArrayList<>();
		toUse.addAll(DIRECTIONS);
		toUse.add(Direction.STAY);
		DIRECTIONS_AND_STAY = Collections.unmodifiableList(toUse);
		toUse = new ArrayList<>();
		toUse.addAll(DIRECTIONS);
		toUse.add(NORTH.move(EAST));
		toUse.add(NORTH.move(WEST));
		toUse.add(SOUTH.move(EAST));
		toUse.add(SOUTH.move(WEST));
		DIRECTIONS_AND_DIAGONALS = toUse;
	}
}
