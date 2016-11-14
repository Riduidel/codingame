package org.ndx.codingame.hypersonic.content;

import org.ndx.codingame.hypersonic.Entity;

public class Bomb extends Entity {
	public final int owner;
	public final int delay;
	public final int range;
	public Bomb(int owner, int x, int y, int delay, int range) {
		super(x, y);
		this.owner = owner;
		this.delay = delay;
		this.range = range;
	}
	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitBomb(this);
	}

	@Override public boolean canBeWalkedOn() { return false; }
	@Override
	public String toString() {
		return "Bomb [owner=" + owner + ", delay=" + delay + ", range=" + range + ", x=" + x + ", y=" + y + "]";
	}
}