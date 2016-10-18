package org.ndx.codingame.hypersonic;

public abstract class Entity implements Content {
	public final int x;
	public final int y;
	public Entity(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override public CanFire canFire() { return CanFire.YES; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Entity [x=" + x + ", y=" + y + "]";
	}
}