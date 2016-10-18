package org.ndx.codingame.hypersonic;

public class Item extends Entity {
	public final int type;

	public Item(int ignored0, int x, int y, int type, int ignored2) {
		super(x, y);
		this.type = type;
	}

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitItem(this);
	}
	@Override public CanFire canFire() { return CanFire.END_PROPAGATION; }

	@Override public boolean canBeWalkedOn() { return true; }
}