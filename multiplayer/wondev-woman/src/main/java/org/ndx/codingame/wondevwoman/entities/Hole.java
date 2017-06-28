package org.ndx.codingame.wondevwoman.entities;

import org.ndx.codingame.wondevwoman.Constants;

public class Hole implements Content {

	public static Content instance = new Hole();

	@Override
	public <Returned> Returned accept(final ContentVisitor<Returned> visitor) {
		return visitor.visitHole(this);
	}

	@Override
	public int getHeight() {
		return Constants.HOLE_HEIGHT;
	}
}
