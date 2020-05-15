package org.ndx.codingame.spring.challenge.entities;

import org.ndx.codingame.spring.challenge.EvolvableConstants;

public class PotentialSmallPill implements Content {

	public static final char CHARACTER = '?';
	public static PotentialSmallPill instance = new PotentialSmallPill();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitPotentialSmallPill(this);
	}

	@Override
	public boolean canBeWalkedOnBy(AbstractPac pac) {
		return true;
	}

	@Override
	public int score() {
		return EvolvableConstants.INTERNAL_SCORE_FOR_POTENTIAL_SMALL_PILL;
	}
	
	@Override
	public String toString() {
		return Character.toString(CHARACTER);
	}
	
	@Override
	public boolean revealsGround() {
		return true;
	}
}
