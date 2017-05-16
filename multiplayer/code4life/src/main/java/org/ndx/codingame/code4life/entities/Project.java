package org.ndx.codingame.code4life.entities;

import java.util.Map;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Project extends MoleculeStore implements ConstructableInUnitTest {
	private boolean completed = false;

	public Project(final Map<Molecule, Integer> map) {
		super();
		addAllAvailable(map);
	}

	public Project(final int a, final int b, final int c, final int d, final int e) {
		this(MoleculeStore.toMap(a, b, c, d, e));
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder();
		returned.append("new Project(")
			.append(MoleculeStore.moleculeMapToArguments(getAvailable()))
			.append(")");
		return returned;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(final boolean completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Project [completed=");
		builder.append(completed);
		builder.append(", getAvailable()=");
		builder.append(getAvailable());
		builder.append("]");
		return builder.toString();
	}
}
