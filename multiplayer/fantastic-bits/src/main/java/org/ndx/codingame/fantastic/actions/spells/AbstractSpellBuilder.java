package org.ndx.codingame.fantastic.actions.spells;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.FantasticStatus;

public abstract class AbstractSpellBuilder<Type extends Entity> {
	private final int cost;

	public AbstractSpellBuilder(final int cost) {
		this.cost = cost;
	}

	protected Collection<Action> testOn(final Entities entities, final FantasticStatus status, final Wizard wizard,
			final Type entity, final SpellStatus spellStatus) {
		final Collection<Action> returned = new ArrayList<>();
		status.setIfNeeded(spellStatus);
		if(status.getMagic()>cost) {
			if(!status.get(spellStatus.getClass()).isAppliedOn(entity)) {
				doTestOn(entities, status, wizard, entity)
					.ifPresent(a -> returned.add(a));
			}
		}
		return returned;
	}

	protected abstract Optional<Action> doTestOn(Entities entities, FantasticStatus status, Wizard wizard, Type entity);
}
