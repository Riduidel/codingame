package org.ndx.codingame.hypersonic;

public class BombDanger implements Content {
	static BombDanger instance = new BombDanger();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitBombDanger(this);
	}

	@Override public CanFire canFire() { return CanFire.YES; }

	@Override public boolean canBeWalkedOn() { return true; }
}