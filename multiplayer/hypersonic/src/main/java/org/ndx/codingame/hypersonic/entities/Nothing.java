package org.ndx.codingame.hypersonic.entities;

public class Nothing implements Content {
	public static Nothing instance = new Nothing();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitNothing(this);
	}

	@Override public CanFire canFire() { return CanFire.YES; }

	@Override public boolean canBeWalkedOn() { return true; }
}