package org.ndx.codingame.fantastic.actions;

import java.util.ArrayList;
import java.util.List;

import org.ndx.codingame.fantastic.actions.spells.AccioSpellBuilder;
import org.ndx.codingame.fantastic.actions.spells.FlipendoSpellBuilder;
import org.ndx.codingame.fantastic.actions.spells.ObliviateSpellBuilder;
import org.ndx.codingame.fantastic.actions.spells.PetrificusSpellBuilder;
import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.EntityVisitor;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;

public class ActionListBuilder implements EntityVisitor<List<Action>> {

	private final Wizard wizard;
	private final Status status;
	private final Entities entities;

	public ActionListBuilder(final Entities allEntities, final Status status, final Wizard w) {
		entities = allEntities;
		this.status = status;
		wizard = w;
	}

	@Override
	public List<Action> visitSnaffle(final Snaffle snaffle) {
		final List<Action> actions = new ArrayList<>();
		if(wizard.holdingSnaffle) {
			if(snaffle.position.equals(wizard.position)) {
				if(snaffle.speed.equals(wizard.speed)) {
					actions.add(new ThrowTo(entities.all(), status, wizard, snaffle));
				} else {
					actions.addAll(new PetrificusSpellBuilder().testOn(entities, status, wizard, snaffle));
					actions.addAll(new AccioSpellBuilder().testOn(entities, status, wizard, snaffle));
				}
			}
		} else {
			actions.addAll(new PetrificusSpellBuilder().testOn(entities, status, wizard, snaffle));
			actions.addAll(new AccioSpellBuilder().testOn(entities, status, wizard, snaffle));
			actions.addAll(new FlipendoSpellBuilder().testOn(entities, status, wizard, snaffle));
			actions.add(new MoveTo(wizard, snaffle));
		}
		return actions;
	}

	@Override
	public List<Action> visitWizard(final Wizard wizard) {
		final List<Action> actions = new ArrayList<>();
		return actions;
	}

	@Override
	public List<Action> visitBludger(final Bludger bludger) {
		final List<Action> actions = new ArrayList<>();
		if(!wizard.holdingSnaffle) {
			actions.addAll(new ObliviateSpellBuilder().testOn(entities, status, wizard, bludger));
		}
		return actions;
	}

}
