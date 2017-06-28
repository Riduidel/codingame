package org.ndx.codingame.wondevwoman.actions;

import org.ndx.codingame.lib2d.discrete.Direction;

public class Push extends WonderAction {

	public Push(final int gamerIndex, final Direction direction1, final Direction direction2) {
		super("PUSH&BUILD", "p", gamerIndex, direction1, direction2);
	}

	public Push(final int gamerIndex, final String direction1, final String direction2) {
		super("PUSH&BUILD", "p", gamerIndex, direction1, direction2);
	}

	@Override
	public <Type> Type accept(final WonderActionVisitor<Type> wonderActionVisitor) {
		return wonderActionVisitor.visitPush(this);
	}

	public Direction getPush() {
		return getFirstDirection();
	}

}
