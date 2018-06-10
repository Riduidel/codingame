package org.ndx.codingame.fantastic.actions.spells;

import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.status.FantasticStatus;

public abstract class AbstractSpell<Type extends Entity> implements Action {

	public final Type target;
	public final int cost;
	private final String name;

	public AbstractSpell(final String string, final Type entity, final int cost) {
		name = string;
		target = entity;
		this.cost = cost;
	}

	@Override
	public String toCommand() {
		return String.format("%s %s", name, target.id);
	}

	@Override
	public void updateStatus(final FantasticStatus status) {
		status.setMagic(status.getMagic()-cost);
	}

	@Override
	public String toString() {
		return String.format("%s [name=%s, target=%s, score=%s]", getClass().getSimpleName(), name, target, getScore());
	}
	
}
