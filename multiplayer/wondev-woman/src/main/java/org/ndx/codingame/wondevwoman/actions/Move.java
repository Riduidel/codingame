package org.ndx.codingame.wondevwoman.actions;

import org.ndx.codingame.lib2d.discrete.Direction;

public class Move extends WonderAction {

	public Move(final int index, final Direction direction1, final Direction direction2) {
		super("MOVE&BUILD", "m", index, direction1, direction2);
	}

	public Move(final int index, final String direction1, final String direction2) {
		super("MOVE&BUILD", "m", index, direction1, direction2);
	}

	@Override
	public <Type> Type accept(final WonderActionVisitor<Type> wonderActionVisitor) {
		return wonderActionVisitor.visitMove(this);
	}

	public Direction getMove() {
		return getFirstDirection();
	}
}
