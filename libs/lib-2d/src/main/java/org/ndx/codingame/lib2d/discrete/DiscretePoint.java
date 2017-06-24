package org.ndx.codingame.lib2d.discrete;

import org.ndx.codingame.lib2d.PointBuilder;
import org.ndx.codingame.lib2d.base.AbstractPoint;

public class DiscretePoint extends AbstractPoint implements PointBuilder<DiscretePoint>{
	public final int x;
	public final int y;
	public DiscretePoint(final int x, final int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public DiscretePoint(final AbstractPoint other) {
		super();
		x = (int) other.getX();
		y = (int) other.getY();
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
	public String toString() {
		return "DiscretePoint [x=" + x + ", y=" + y + "]";
	}
	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix)  {
		final StringBuilder returned = new StringBuilder();
		returned.append("new ").append(getClass().getSimpleName()).append("(").append(x).append(", ").append(y).append(")");
		return returned;
	}
	@Override
	public DiscretePoint build(final double x, final double y) {
		return new DiscretePoint((int)x, (int)y);
	}
}
