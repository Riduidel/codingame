package org.ndx.codingame.fantastic.spells;

import java.util.Collection;

import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class AccioSpell extends Spell {
	public static class AccioStatus extends SpellStatus {
		public AccioStatus() {
			super(6);
		}
	}
	public AccioSpell() {
		super(20, "ACCIO", new AccioStatus());
	}

	@Override
	public SpellContext shouldCastThat(final Status status, final Wizard wizard, final Entities entities) {
		if(!wizard.isAttacking()) {
			final Collection<Snaffle> snaffles = entities.sortSnafflesFor(wizard).values();
			return snaffles.stream()
				.filter(s -> wizard.position.distance2To(s.position)>Wizard.RADIUS)
				.filter(s -> {
					final Segment expected = new Segment(s.position, 
							new ContinuousPoint(s.position.x+s.speed.x*3, s.position.y+s.speed.y*3));
					return expected.intersectsWith(wizard.getDefendedGoal());
				})
				.findFirst()
				.map(s -> new SpellContext(s))
				.orElse(SpellContext.NO);
		}
		return SpellContext.NO;
	}
	
	@Override
	public String cast(final Status status, final SpellContext context) {
		status.get(AccioStatus.class).applyOn(context.entity);
		return super.cast(status, context);
	}
}
