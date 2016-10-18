package org.ndx.codingame.hypersonic;

public class Nothing implements Content {
	static Nothing instance = new Nothing();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitNothing(this);
	}

	@Override public CanFire canFire() { return CanFire.YES; }

	@Override public boolean canBeWalkedOn() { return true; }
}