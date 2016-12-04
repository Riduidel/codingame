package org.ndx.codingame.fantastic.actions.spells;

import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.actions.ActionVisitor;
import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;

public class ObliviateSpell extends AbstractSpell<Bludger> {

	public static final String OBLIVIATE = "OBLIVIATE";
	private final List<Wizard> myTeam;

	public ObliviateSpell(final Entities entities, final List<Wizard> myTeamisNearBludger, final Bludger entity) {
		super(OBLIVIATE, entity, Constants.MAGIC_OBLIVIATE_COST);
		myTeam = myTeamisNearBludger;
	}

	@Override
	public double getScore() {
		return target.position.minDistance2To(myTeam.stream().map(w -> w.position).collect(Collectors.toList()))/Constants.WIZARD_MAX_SPEED;
	}

	@Override
	public boolean conflictsWith(final Status status, final Action current) {
		return current.accept(new SpellConflictAdapter(status, this) {
			
			@Override
			protected Boolean doVisit(final ObliviateSpell obliviateSpell) {
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
		status.get(ObliviateStatus.class).applyOn(target);
	}
}
