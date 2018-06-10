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
import org.ndx.codingame.fantastic.status.FantasticStatus;
import org.ndx.codingame.fantastic.status.TeamStatus;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;

public class FlipendoSpellBuilder extends AbstractSpellBuilder<Entity>{

	public FlipendoSpellBuilder() {
		super(Constants.MAGIC_FLIPENDO_COST);
	}

	public Collection<? extends Action> testOn(final Entities entities, final FantasticStatus status, final Wizard wizard, final Entity entity) {
		return testOn(entities, status, wizard, entity, new FlipendoStatus());
	}

	@Override
	protected Optional<Action> doTestOn(final Entities entities, final FantasticStatus status, final Wizard wizard, final Entity entity) {
		final Segment attacked = status.get(TeamStatus.class).getAttacked();
		final List<ContinuousPoint> wizards = entities.getAllWizards().stream()
			.map(w -> w.position)
			.collect(Collectors.toList());
		// entity is not on wizard
		if(!wizards.contains(entity.position)) {
			// entity is in direction of attacked goal
			if(attacked.distance2To(entity.position)<attacked.distance2To(wizard.position)) {
				return Optional.of(new FlipendoSpell(attacked, wizard, entity));
			}
		}
		return Optional.empty();
	}

}
