package org.ndx.codingame.fantastic.entities;

import org.ndx.codingame.fantastic.Constants;

public class Snaffle extends Entity {
	public boolean isATarget;

	public Snaffle(final int id, final double x, final double y, final double vx, final double vy) {
		super(id, x, y, vx, vy);
	}

	@Override
	public <Type> Type accept(final EntityVisitor<Type> visitor) {
		return visitor.visitSnaffle(this);
	}

	@Override
	public double getRadius() {
		return Constants.RADIUS_SNAFFLE;
	}

	@Override
	public String toString() {
		return String.format("Snaffle [id=%s]", id);
	}
}
