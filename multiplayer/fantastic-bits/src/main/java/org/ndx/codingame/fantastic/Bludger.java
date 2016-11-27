package org.ndx.codingame.fantastic;

public class Bludger extends Entity {
	public static final int RADIUS = 200;

	public Bludger(int id, double x, double y, double vx, double vy) {
		super(id, x, y, vx, vy);
	}

	@Override
	public <Type> Type accept(EntityVisitor<Type> visitor) {
		return visitor.visitBludger(this);
	}

	@Override
	protected double getRadius() {
		return RADIUS;
	}

}
