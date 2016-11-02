package org.ndx.codingame.lib2d.continuous;

import org.ndx.codingame.lib2d.base.AbstractPoint;

public class ContinuousPoint extends AbstractPoint {
	public final double x;
	public final double y;
	public ContinuousPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	@Override
	public double getX() {
		return x;
	}
	@Override
	public double getY() {
		return y;
	}
}
