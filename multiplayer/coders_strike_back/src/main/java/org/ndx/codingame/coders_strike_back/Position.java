package org.ndx.codingame.coders_strike_back;

import org.ndx.codingame.lib2d.Point;
import org.ndx.codingame.lib2d.PointBuilder;

public class Position extends Point implements PointBuilder<Position> {

	public Position(double x, double y) {
		super(x, y);
	}

	@Override
	public Position build(double x, double y) {
		return new Position(x, y);
	}

	public String goTo(int thrust) {
		return String.format("%d %d %d", (int) x, (int) y, thrust);
	}

	public String goTo(String thrust) {
		return String.format("%d %d %s", (int) x, (int) y, thrust);
	}
}
