package org.ndx.codingame.fantastic.spells;

import java.util.Collection;

import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class FlipendoSpell extends Spell {
	public static class FlipendoStatus extends SpellStatus {
		public FlipendoStatus() {
			super(3);
		}
	}
	
	public FlipendoSpell() {
		super(20, "FLIPENDO", new FlipendoStatus());
	}
	@Override
	public SpellContext shouldCastThat(final Status status, final Wizard wizard, final Entities entities) {
		if(status.getMagic()>30) {
			final Collection<Snaffle> snaffles = entities.sortSnafflesFor(wizard).values();
			return snaffles.stream()
				.filter(s -> wizard.position.distance2To(s.position)>Wizard.RADIUS)
				.filter(s -> {
					final Line expected = new Line(s.position, 
							new ContinuousPoint(s.position.x+s.speed.x*2, s.position.y+s.speed.y*2));
					return expected.intersectsWith(wizard.getAttackedGoal()) && wizard.getAttackedGoal().intersectsWith(expected);
				})
				.filter(s -> {
					final Line expected = new Line(wizard.position, 
							s.position);
					return expected.intersectsWith(wizard.getAttackedGoal()) && wizard.getAttackedGoal().intersectsWith(expected);
				})
				.filter(s -> Math.min(s.position.distance2To(wizard.getAttackedGoal().first), s.position.distance2To(wizard.getAttackedGoal().first))>400)
				.findFirst()
				.map(s -> new SpellContext(s))
				.orElse(SpellContext.NO);
		}
		return SpellContext.NO;
	}
	
	@Override
	public String cast(final Status status, final SpellContext context) {
		status.get(FlipendoStatus.class).applyOn(context.entity);
		return super.cast(status, context);
	}
}
