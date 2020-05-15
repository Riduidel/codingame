package org.ndx.codingame.spring.challenge.entities;

import org.ndx.codingame.spring.challenge.EvolvableConstants;

public class Pac extends AbstractPac {

	public Pac(int x, int y, int pacId, boolean mine, Type type, int speedTurnsLeft, int abilityCooldown) {
		super(x, y, pacId, mine, type, speedTurnsLeft, abilityCooldown);
	}

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitPac(this);
	}

	@Override
	public String toString() {
		return "Pac [id=" + id + ", type=" + type + ", x=" + x + ", y=" + y + "]";
	}

	@Override
	public int score() {
		return EvolvableConstants.INTERNAL_SCORE_FOR_PAC;
	}
	
	@Override
	public Content advanceOneTurn() {
		return new PacTrace(x, y, id, mine, type, speedTurnsLeft, abilityCooldown);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + id;
		result = prime * result + (mine ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractPac other = (AbstractPac) obj;
		if (id != other.id)
			return false;
		if (mine != other.mine)
			return false;
		return true;
	}

}
