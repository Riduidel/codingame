package org.ndx.codingame.hypersonic.content;

import org.ndx.codingame.hypersonic.CanFire;

public class Wall implements Content {
	public static Wall instance = new Wall();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitWall(this);
	}

	@Override public CanFire canFire() { return CanFire.NOT; }

	@Override public boolean canBeWalkedOn() { return false; }
}