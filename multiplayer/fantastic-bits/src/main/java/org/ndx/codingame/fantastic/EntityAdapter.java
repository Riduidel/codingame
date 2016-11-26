package org.ndx.codingame.fantastic;

public class EntityAdapter<Type> implements EntityVisitor<Type> {
	protected Type returned = null;

	public EntityAdapter() {
	}

	public EntityAdapter(Type defaultValue) {
		returned = defaultValue;
	}

	@Override
	public Type visitSnaffle(Snaffle snaffle) {
		return returned;
	}

	@Override
	public Type visitWizard(Wizard wizard) {
		return returned;
	}

	@Override
	public Type visitBludger(Bludger bludger) {
		return returned;
	}

}
