package org.ndx.codingame.fantastic.actions.spells;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.FantasticStatus;

public class ObliviateSpellBuilder extends AbstractSpellBuilder<Bludger> {

	public ObliviateSpellBuilder() {
		super(Constants.MAGIC_OBLIVIATE_COST);
	}

	public Collection<? extends Action> testOn(final Entities entities, final FantasticStatus status, final Wizard wizard, final Bludger bludger) {
		return testOn(entities, status, wizard, bludger, new ObliviateStatus());
	}

	@Override
	protected Optional<Action> doTestOn(final Entities entities, final FantasticStatus status, final Wizard wizard, final Bludger entity) {
		final List<Wizard> myTeamisNearBludger = entities.getAllWizards().stream()
				.sorted(new Entity.ByDistanceTo(entity))
				.limit(2)
				.filter(w -> w.teamId==status.getTeam())
				.collect(Collectors.toList());
			if(myTeamisNearBludger.size()==2) {
				for(final Wizard w : myTeamisNearBludger) {
					if(entity.direction.toLine().intersectsWith(w.getCircle())) {
						return Optional.of(new ObliviateSpell(entities, myTeamisNearBludger, entity));
					}
				}
			}
		return Optional.empty();
	}

}
