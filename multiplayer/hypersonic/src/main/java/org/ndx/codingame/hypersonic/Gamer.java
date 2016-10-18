package org.ndx.codingame.hypersonic;

public class Gamer extends Entity {
	public final int id;
	public final int bombs;
	public final int range;
	public Gamer(int id, int x, int y, int bombs, int range) {
		super(x, y);
		this.id = id;
		this.bombs = bombs;
		this.range = range;
	}
	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitGamer(this);
	}

	@Override public boolean canBeWalkedOn() { return true; }
	@Override
	public String toString() {
		return "Gamer [id=" + id + ", bombs=" + bombs + ", range=" + range + ", x=" + x + ", y=" + y + "]";
	}
}