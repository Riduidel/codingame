package org.ndx.codingame.spring.challenge.entities;

import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.EvolvableConstants;

public class BigPill extends AbstractDistinctContent {

	public static final char CHARACTER = 'O';

	public BigPill(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public BigPill(AbstractPoint other) {
		super(other);
	}

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitBigPill(this);
	}

	@Override
	public boolean canBeWalkedOn() {
		return true;
	}

	@Override
	public int score() {
		return EvolvableConstants.INTERNAL_SCORE_FOR_BIG_PILL;
	}

}
