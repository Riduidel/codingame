package org.ndx.codingame.spring.challenge.entities;

import org.ndx.codingame.spring.challenge.EvolvableConstants;

public class PacTrace extends AbstractPac implements Content {

	public static final char ENEMY = 'e';
	public static final char MINE = 'm';

	public PacTrace(int x, int y, int pacId, boolean mine, Type type,
			int speedTurnsLeft, int abilityCooldown) {
		super(x, y, pacId, mine, type, speedTurnsLeft, abilityCooldown);
	}

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitPacTrace(this);
	}

	@Override
	public boolean canBeWalkedOnBy(AbstractPac pac) {
		return true;
	}

	@Override
	public int score() {
		return EvolvableConstants.INTERNAL_SCORE_FOR_PAC_TRACE;
	}
	
	@Override
	public String toString() {
		if(mine) {
			return Character.toString(MINE);
		} else {
			return Character.toString(ENEMY);
		}
	}
}
