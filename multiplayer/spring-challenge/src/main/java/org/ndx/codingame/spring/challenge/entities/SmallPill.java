package org.ndx.codingame.spring.challenge.entities;

import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.EvolvableConstants;

public class SmallPill extends AbstractDistinctContent {

	public static final char CHARACTER = '.';

	public SmallPill(int x, int y) {
		super(x, y);
	}

	public SmallPill(AbstractPoint other) {
		super(other);
	}

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitSmallPill(this);
	}

	@Override
	public boolean canBeWalkedOn() {
		return true;
	}

	@Override
	public int score() {
		return EvolvableConstants.INTERNAL_SCORE_FOR_SMALL_PILL;
	}

	@Override
	public String toString() {
		return Character.toString(CHARACTER);
	}
	
	@Override
	public Content advanceOneTurn() {
		return PotentialSmallPill.instance;
	}
}
