package org.ndx.codingame.code4life.entities;

import java.util.Map;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Project implements ConstructableInUnitTest {
	private boolean completed = false;

	public final Map<Molecule, Integer> required;

	public Project(final Map<Molecule, Integer> map) {
		required = map;
	}

	public Project(final int a, final int b, final int c, final int d, final int e) {
		this(MoleculeStore.toMap(a, b, c, d, e));
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder();
		returned.append("new Project(")
			.append(MoleculeStore.moleculeMapToArguments(required))
			.append(")");
		return returned;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(final boolean completed) {
		this.completed = completed;
	}
}
