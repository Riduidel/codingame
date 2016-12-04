package org.ndx.codingame.fantastic.entities;

import org.ndx.codingame.fantastic.Constants;

public class Bludger extends Entity {
	public Bludger(final int id, final double x, final double y, final double vx, final double vy) {
		super(id, x, y, vx, vy);
	}

	@Override
	public <Type> Type accept(final EntityVisitor<Type> visitor) {
		return visitor.visitBludger(this);
	}

	@Override
	public double getRadius() {
		return Constants.RADIUS_BLUDGER;
	}

	@Override
	public String toString() {
		return String.format("Bludger [id=%s]", id);
	}

}
