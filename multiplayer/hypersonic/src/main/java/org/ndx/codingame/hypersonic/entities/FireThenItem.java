package org.ndx.codingame.hypersonic.entities;

public class FireThenItem extends Fire {
	public static FireThenItem instance = new FireThenItem();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitFireThenItem(this);
	}

	@Override public CanFire canFire() { return CanFire.YES; }

	@Override public boolean canBeWalkedOn() { return false; }
}