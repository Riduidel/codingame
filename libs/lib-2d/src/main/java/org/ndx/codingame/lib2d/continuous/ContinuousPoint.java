package org.ndx.codingame.lib2d.continuous;

import org.ndx.codingame.lib2d.PointBuilder;
import org.ndx.codingame.lib2d.base.AbstractPoint;

public class ContinuousPoint extends AbstractPoint implements PointBuilder<ContinuousPoint>{
	public final double x;
	public final double y;
	public ContinuousPoint(final double x, final double y) {
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
	@Override
	public ContinuousPoint build(final double x, final double y) {
		return new ContinuousPoint(x, y);
	}
	public ContinuousPoint moveOf(final ContinuousPoint speed) {
		return super.moveOf(speed, this);
	}
	public ContinuousPoint moveOf(final double x2, final double y2) {
		return super.moveOf(x2, y2, this);
	}
	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix)  {
		final StringBuilder returned = new StringBuilder();
		returned.append("new ").append(getClass().getSimpleName()).append("(").append(x).append(", ").append(y).append(")");
		return returned;
	}
}
