package org.ndx.codingame.hypersonic.entities;

public class Wall implements Content {
	public static Wall instance = new Wall();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitWall(this);
	}

	@Override public CanFire canFire() { return CanFire.NOT; }

	@Override public boolean canBeWalkedOn() { return false; }
}