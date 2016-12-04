package org.ndx.codingame.fantastic.actions.spells;

import org.ndx.codingame.fantastic.actions.ActionAdapter;
import org.ndx.codingame.fantastic.status.Status;

public abstract class SpellConflictAdapter extends ActionAdapter<Boolean>{
	private final Status status;
	private final AbstractSpell source;

	public SpellConflictAdapter(final Status status, final AbstractSpell source) {
		super(false);
		this.status = status;
		this.source = source;
	}

	private boolean canInvokeBoth(final AbstractSpell other) {
		return status.getMagic()-source.cost-other.cost>=0;
	}

	@Override
	public final Boolean visit(final ObliviateSpell obliviateSpell) {
		if(canInvokeBoth(obliviateSpell)) {
			return doVisit(obliviateSpell);
		}
		return true;
	}

	protected Boolean doVisit(final ObliviateSpell obliviateSpell) {
		return false;
	}

	@Override
	public Boolean visit(final AccioSpell accioSpell) {
		if(canInvokeBoth(accioSpell)) {
			return doVisit(accioSpell);
		}
		return true;
	}

	protected Boolean doVisit(final AccioSpell accioSpell) {
		return false;
	}
	
	@Override
	public final Boolean visit(final FlipendoSpell flipendoSpell) {
		if(canInvokeBoth(flipendoSpell)) {
			return doVisit(flipendoSpell);
		}
		return true;
	}

	protected Boolean doVisit(final FlipendoSpell flipendoSpell) {
		return false;
	}

	@Override
	public Boolean visit(final PetrificusSpell petrificus) {
		if(canInvokeBoth(petrificus)) {
			return doVisit(petrificus);
		}
		return true;
	}

	protected Boolean doVisit(final PetrificusSpell petrificus) {
		return false;
	}
}
