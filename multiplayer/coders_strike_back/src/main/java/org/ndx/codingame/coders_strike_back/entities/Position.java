package org.ndx.codingame.coders_strike_back.entities;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Position extends ContinuousPoint {

	public Position(final double x, final double y) {
		super(x, y);
	}

	@Override
	public Position build(final double x, final double y) {
		return new Position(x, y);
	}

	public String goTo(final int thrust) {
		return String.format("%d %d %d", (int) x, (int) y, thrust);
	}

	public String goTo(final String thrust) {
		return String.format("%d %d %s", (int) x, (int) y, thrust);
	}
}
