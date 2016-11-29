package org.ndx.codingame.fantastic.spells;

import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.fantastic.status.StatusElement;

public class ObliviateSpell extends Spell {
	public static class ObliviateStatus extends SpellStatus implements StatusElement {

		public ObliviateStatus() {
			super(3);
		}
	}
	
	public ObliviateSpell() {
		super(5, "OBLIVIATE", new ObliviateStatus());
	}
	/**
	 * if one of the Bludgers has as targets our two wizards, oblivate it
	 */
	@Override
	public SpellContext shouldCastThat(Status status, Wizard wizard, Entities entities) {
		ObliviateStatus obliviated = status.get(ObliviateStatus.class);
		for(Bludger b : entities.getBludgers()) {
			if(!obliviated.isAppliedOn(b)) {
				List<Wizard> myTeamisNearBludger = entities.getAllWizards().stream()
					.sorted(new Entity.ByDistanceTo(b))
					.limit(2)
					.filter(w -> w.teamId==status.getTeam())
					.collect(Collectors.toList());
				if(myTeamisNearBludger.size()==2) {
					System.err.println(String.format("The two nearest wizards of Bludger %s are mine !", b));
					if(myTeamisNearBludger.get(0).position.distance2To(b.position)<1000) {
						System.err.println(String.format("And one is definitely too near"));
						return new SpellContext(b);
					}
				}
			}
		}
		return SpellContext.NO;
	}
	
	@Override
	public String cast(Status status, SpellContext context) {
		status.get(ObliviateStatus.class).applyOn(context.entity);
		return super.cast(status, context);
	}
}
