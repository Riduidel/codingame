package org.ndx.codingame.spring.challenge.entities;

import org.ndx.codingame.spring.challenge.EvolvableConstants;

public class PacTrace extends AbstractDistinctContent implements Content {

	private int id;
	private Type type;

	public PacTrace(int x, int y, int id, Type type) {
		super(x, y);
		this.id = id;
		this.type = type;
	}

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitPacTrace(this);
	}

	@Override
	public boolean canBeWalkedOn() {
		return true;
	}

	@Override
	public int score() {
		return EvolvableConstants.INTERNAL_SCORE_FOR_PAC_TRACE;
	}

}
