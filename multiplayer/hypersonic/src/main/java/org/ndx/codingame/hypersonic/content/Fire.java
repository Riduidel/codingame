package org.ndx.codingame.hypersonic.content;

import org.ndx.codingame.hypersonic.CanFire;

public class Fire implements Content {
	public static Fire instance = new Fire();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitFire(this);
	}

	@Override public CanFire canFire() { return CanFire.YES; }

	@Override public boolean canBeWalkedOn() { return true; }
}