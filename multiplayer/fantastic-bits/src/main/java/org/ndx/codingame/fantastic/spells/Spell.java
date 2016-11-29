package org.ndx.codingame.fantastic.spells;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.MagicStatus;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public enum Spell {
	FLIPENDO(20) {
	},
	ACCIO(20) {
		@Override
		public SpellContext shouldCastThat(Status status, Wizard wizard, Entities entities) {
			if(!wizard.isAttacking()) {
				AccioStatus spellStatus = status.get(AccioStatus.class);
				if(spellStatus==null) {
					spellStatus = new AccioStatus();
					status.set(spellStatus);
				}
				final Collection<Snaffle> snaffles = entities.sortSnafflesFor(wizard).values();
				return snaffles.stream()
					.filter(s -> {
						Segment expected = new Segment(s.position, 
								new ContinuousPoint(s.position.x+s.speed.x*2, s.position.y+s.speed.y*2));
						return expected.intersectsWith(wizard.getDefendedGoal());
					})
					.findFirst()
					.map(s -> new SpellContext(s))
					.orElse(SpellContext.NO);
			}
			return SpellContext.NO;
		}
		
		@Override
		public String cast(Status status, SpellContext context) {
			status.get(AccioStatus.class).applyOn(context.entity);
			return super.cast(status, context);
		}
	},
	PETRIFICUS(10) {
	},
	OBLIVIATE(5) {
		/**
		 * if one of the Bludgers has as targets our two wizards, oblivate it
		 */
		@Override
		public SpellContext shouldCastThat(Status status, Wizard wizard, Entities entities) {
			ObliviateStatus obliviated = status.get(ObliviateStatus.class);
			if(obliviated==null) {
				obliviated = new ObliviateStatus();
				status.set(obliviated);
			}
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
