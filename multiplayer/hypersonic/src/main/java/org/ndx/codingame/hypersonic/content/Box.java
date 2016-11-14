package org.ndx.codingame.hypersonic.content;

import org.ndx.codingame.hypersonic.CanFire;

public class Box implements Content {
	public static Box instance = new Box();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitBox(this);
	}

	@Override public CanFire canFire() { return CanFire.END_PROPAGATION; }

	@Override public boolean canBeWalkedOn() { return false; }
}