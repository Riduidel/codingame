package org.ndx.codingame.fantastic.actions.spells;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.Playground;
import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.actions.ActionVisitor;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.lib2d.shapes.Vector;

public class AccioSpell extends AbstractSpell {
	public static final String ACCIO = "ACCIO";
	private double score = -1;
	private final Wizard wizard;
	private final Segment defended;

	public AccioSpell(final Segment defended, final Wizard wizard, final Entity entity) {
		super(ACCIO, entity, Constants.MAGIC_ACCIO_COST);
		this.defended = defended;
		this.wizard = wizard;
	}

	@Override
	public double getScore() {
		if(score<0) {
			final Vector extent = new Vector(wizard.direction.second, target.direction.second);
			final Vector extendedMove = new Vector(target.position, 
					new ContinuousPoint(target.position.x+target.speed.x*3, target.position.x+target.speed.x*3));
			if(defended.intersectsWith(extendedMove.toLine())) {
				score = Math.max(Playground.WIDTH-Math.abs(2*extent.getX()), 0);
				if(defended.intersectsWith(extendedMove)) {
					score *= 2;
				}
			} else {
				score = -cost;
			}
		}
		return score;
	}

	@Override
	public boolean conflictsWith(final Status status, final Action current) {
		return current.accept(new SpellConflictAdapter(status, this) {

			@Override
			protected Boolean doVisit(final AccioSpell accioSpell) {
				return true;
			}
		});
	}

	@Override
	public <Type> Type accept(final ActionVisitor<Type> visitor) {
		return visitor.visit(this);
	}

	@Override
	public void updateStatus(final Status status) {
		super.updateStatus(status);
		status.get(AccioStatus.class).applyOn(target);
	}
}
