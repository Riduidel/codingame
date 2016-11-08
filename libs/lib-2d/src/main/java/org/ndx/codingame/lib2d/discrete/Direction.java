package org.ndx.codingame.lib2d.discrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Direction extends DiscretePoint {
	public static final List<Direction> DIRECTIONS = Collections.unmodifiableList(Arrays.asList(
			new Direction(0, -1, "UP"),
			new Direction(1, 0, "RIGHT"),
			new Direction(-1, 0, "LEFT"),
			new Direction(0, 1, "DOWN")
			));
	public static final List<Direction> DIRECTIONS_AND_STAY;
	
	static {
		List<Direction> toUse = new ArrayList<Direction>();
		toUse.addAll(DIRECTIONS);
		toUse.add(new Direction(0, 0, "STAY"));
		DIRECTIONS_AND_STAY = Collections.unmodifiableList(toUse);
	}

	public final String name;

	public Direction(int x, int y, String name) {
		super(x, y);
		this.name = name;
	}

	public ScoredDirection move(DiscretePoint position) {
		return new ScoredDirection(x+position.x, y+position.y, name);
	}

	@Override
	public String toString() {
		return "Direction [x=" + x + ", y=" + y + ", name=" + name + "]";
	}

}
