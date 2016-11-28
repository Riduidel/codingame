package org.ndx.codingame.fantastic.spells;

import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.MagicStatus;
import org.ndx.codingame.fantastic.status.Status;

public enum Spell {
	FLIPENDO(20) {
	},
	ACCIO(20) {
	},
	PETRIFICUS(10) {
	},
	OBLIVIATE(5) {
		/**
		 * if one of the Bludgers has as targets our two wizards, oblivate it
		 */
		@Override
		public SpellContext shouldCastThat(Status status, Wizard wizard, Entities entities) {
			for(Bludger b : entities.getBludgers()) {
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
			return SpellContext.NO;
		}
	};
	
	private int required;

	private Spell(int requiredMagic) {
		this.required = requiredMagic;
	}

	public final SpellContext shouldCast(Status status, Wizard wizard, Entities entities) {
		if(status.get(MagicStatus.class).getMagic()>required) {
			return shouldCastThat(status, wizard, entities);
		}
		return SpellContext.NO;
	}

	protected SpellContext shouldCastThat(Status status, Wizard wizard, Entities entities) {
		return SpellContext.NO;
	}

	public String cast(Status status, SpellContext context) {
		status.get(MagicStatus.class).cast(required);
		return String.format("%s %d", name(), context.entity.id);
	}
}