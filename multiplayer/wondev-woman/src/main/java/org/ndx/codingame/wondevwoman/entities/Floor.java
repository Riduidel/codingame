package org.ndx.codingame.wondevwoman.entities;

public class Floor implements Content {
	public static Floor heightToFloor(final byte height) {
		return new Floor(height-'0');
	}
	public final int height;

	public Floor(final int height) {
		super();
		this.height = height;
	}

	@Override
	public <Returned> Returned accept(final ContentVisitor<Returned> visitor) {
		return visitor.visitFloor(this);
	}

	public char heightToChar() {
		return (char) (height+'0');
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Floor other = (Floor) obj;
		if (height != other.height) {
			return false;
		}
		return true;
	}

}
