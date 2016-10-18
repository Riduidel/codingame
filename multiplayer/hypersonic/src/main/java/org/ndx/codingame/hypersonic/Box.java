package org.ndx.codingame.hypersonic;

public class Box implements Content {
	static Box instance = new Box();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitBox(this);
	}

	@Override public CanFire canFire() { return CanFire.END_PROPAGATION; }

	@Override public boolean canBeWalkedOn() { return false; }
}