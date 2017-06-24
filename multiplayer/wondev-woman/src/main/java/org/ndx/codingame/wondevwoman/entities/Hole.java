package org.ndx.codingame.wondevwoman.entities;

public class Hole implements Content {

	public static Content instance = new Hole();

	@Override
	public <Returned> Returned accept(final ContentVisitor<Returned> visitor) {
		return visitor.visitHole(this);
	}
}
