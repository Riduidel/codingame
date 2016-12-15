package org.ndx.codingame.fantastic.actions.spells;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.Playground;
import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.actions.ActionVisitor;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.lib2d.shapes.Segment;

public class PetrificusSpell extends AbstractSpell {
	public static final String PETRIFICUS = "PETRIFICUS";
	private double score = -1;
	private final Segment defended;

	public PetrificusSpell(final Segment defended, final Entity entity) {
		super(PETRIFICUS, entity, Constants.MAGIC_PETRIFICUS_COST);
		this.defended = defended;
	}

	@Override
	public double getScore() {
		if(score<0) {
			score = Playground.WIDTH-defended.distance2To(target.position);
		}
		return score;
	}

	@Override
	public boolean conflictsWith(final Status status, final Action current) {
		return current.accept(new SpellConflictAdapter(status, this) {
			@Override
			protected Boolean doVisit(final PetrificusSpell accioSpell) {
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
		status.get(FlipendoStatus.class).applyOn(target);
	}

}
