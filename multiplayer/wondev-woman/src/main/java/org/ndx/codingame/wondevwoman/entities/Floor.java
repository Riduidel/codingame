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

}
