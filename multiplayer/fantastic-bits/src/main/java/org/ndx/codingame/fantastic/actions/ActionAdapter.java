package org.ndx.codingame.fantastic.actions;

import org.ndx.codingame.fantastic.actions.spells.AccioSpell;
import org.ndx.codingame.fantastic.actions.spells.FlipendoSpell;
import org.ndx.codingame.fantastic.actions.spells.ObliviateSpell;
import org.ndx.codingame.fantastic.actions.spells.PetrificusSpell;

public class ActionAdapter<Type> implements ActionVisitor<Type> {
	private Type returned = null;
	
	public ActionAdapter() {
		
	}
	
	public ActionAdapter(final Type value) {
		this.returned = value;
	}

	@Override
	public Type visit(final MoveTo moveTo) {
		return returned;
	}

	@Override
	public Type visit(final ThrowTo throwTo) {
		return returned;
	}

	@Override
	public Type visit(final ObliviateSpell obliviateSpell) {
		return returned;
	}

	@Override
	public Type visit(final AccioSpell accioSpell) {
		return returned;
	}

	@Override
	public Type visit(final FlipendoSpell flipendoSpell) {
		return returned;
	}

	@Override
	public Type visit(final PetrificusSpell petrificus) {
		return returned;
	}

}
