package org.ndx.codingame.fantastic;

public class Snaffle extends Entity {
	public static final int RADIUS = 150;
	
	public boolean isATarget;

	public Snaffle(int id, double x, double y, double vx, double vy) {
		super(id, x, y, vx, vy);
	}

	@Override
	public <Type> Type accept(EntityVisitor<Type> visitor) {
		return visitor.visitSnaffle(this);
	}

}
