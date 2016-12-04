package org.ndx.codingame.fantastic.actions.spells;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.fantastic.status.TeamStatus;
import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class AccioSpellBuilder extends AbstractSpellBuilder<Entity>{

	public AccioSpellBuilder() {
		super(Constants.MAGIC_ACCIO_COST);
	}

	public Collection<? extends Action> testOn(final Entities entities, final Status status, final Wizard wizard, final Entity entity) {
		return testOn(entities, status, wizard, entity, new AccioStatus());
	}

	@Override
	protected Optional<Action> doTestOn(final Entities entities, final Status status, final Wizard wizard, final Entity entity) {
		final Segment defended = status.get(TeamStatus.class).getDefended();
		final List<ContinuousPoint> wizards = entities.getAllWizards().stream()
			.map(w -> w.position)
			.collect(Collectors.toList());
		if(!wizards.contains(entity.position)) {
			if(defended.distance2To(entity.position)<defended.distance2To(wizard.position)) {
				return Optional.of(new AccioSpell(defended, wizard, entity));
			}
		}
		return Optional.empty();
	}

}
