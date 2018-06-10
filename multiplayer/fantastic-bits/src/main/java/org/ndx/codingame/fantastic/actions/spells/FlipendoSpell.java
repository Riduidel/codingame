package org.ndx.codingame.fantastic.actions.spells;

import java.util.Collection;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.Playground;
import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.actions.ActionVisitor;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.FantasticStatus;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.lib2d.shapes.Vector;

public class FlipendoSpell extends AbstractSpell {
	public static final String FLIPENDO = "FLIPENDO";
	private double score = -1;
	private final Wizard wizard;
	private final Segment attacked;

	public FlipendoSpell(final Segment attacked, final Wizard wizard, final Entity entity) {
		super(FLIPENDO, entity, Constants.MAGIC_FLIPENDO_COST);
		this.attacked = attacked;
		this.wizard = wizard;
	}

	@Override
	public double getScore() {
		if(score<0) {
			final Vector extent = new Vector(wizard.direction.second, target.direction.second);
			score = Math.max(Playground.WIDTH-Math.abs(2*extent.getX()), 0);
			if(!attacked.intersectsWith(extent.toLine())) {
				final Collection<ContinuousPoint> intersectionWith = attacked.toLine().intersectionWith(extent);
				intersectionWith.stream()
					.forEach(point -> score /= attacked.distance2To(point));
			}
		}
		return score;
	}

	@Override
	public boolean conflictsWith(final FantasticStatus status, final Action current) {
		return current.accept(new SpellConflictAdapter(status, this) {

			@Override
			protected Boolean doVisit(final FlipendoSpell accioSpell) {
				return true;
			}
		});
	}

	@Override
	public <Type> Type accept(final ActionVisitor<Type> visitor) {
		return visitor.visit(this);
	}

	@Override
	public void updateStatus(final FantasticStatus status) {
		super.updateStatus(status);
		status.get(FlipendoStatus.class).applyOn(target);
	}

}
