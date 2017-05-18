package org.ndx.codingame.code4life.entities;

import java.util.Map;

import org.ndx.codingame.code4life.Constants;
import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Robot extends MoleculeStore implements ConstructableInUnitTest {

	public final Module target;
	public final int eta;
	public final int score;
	public final Map<Molecule, Integer> expertise;

	public Robot(final String target, final int eta, final int score, final Map<Molecule, Integer> counts,
			final Map<Molecule, Integer> expertise) {
		this.target = Module.valueOf(target);
		this.eta = eta;
		this.score = score;
		addAllAvailable(counts);
		this.expertise = expertise;
	}
	public Robot(final String target, final int eta, final int score,
			final int countA,
			final int countB,
			final int countC,
			final int countD,
			final int countE,
			final int expertiseA,
			final int expertiseB,
			final int expertiseC,
			final int expertiseD,
			final int expertiseE) {
		this(target, eta, score,
				MoleculeStore.toMap(countA, countB, countC, countD, countE),
				MoleculeStore.toMap(expertiseA, expertiseB, expertiseC, expertiseD, expertiseE));
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder();
		returned.append("new Robot(")
		.append("\"").append(target.name()).append("\"").append(",\t")
		.append(eta).append(",\t")
		.append(score).append(",\t")
		.append(MoleculeStore.moleculeMapToArguments(getAvailable())).append(", ")
		.append(MoleculeStore.moleculeMapToArguments(expertise)).append(")");
		return returned;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Robot [score=");
		builder.append(score);
		builder.append(",\n getAvailable()=\t");
		builder.append(getAvailable());
		builder.append(",\n      expertise=\t");
		builder.append(expertise);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public Map<Molecule, Integer> findMissingFor(final Map<Molecule, Integer> cost) {
		return MoleculeStore.substract(cost, add(getAvailable(), expertise));
	}

	public Robot redirectTo(final Module direction) {
		return new Robot(direction.name(), direction.distanceTo(target), score,
				getAvailable(), expertise);
	}
	public Robot scoreTo(final Sample toDrop) {
		return new Robot(target.name(), 0, score+toDrop.health,
				getAvailable(), expertise);
	}

	public boolean isFullOfMolecules() {
		return getTotalCount()>=Constants.MAX_MOLECULES;
	}
}
