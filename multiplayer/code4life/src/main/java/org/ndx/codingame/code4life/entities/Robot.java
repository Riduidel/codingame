package org.ndx.codingame.code4life.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Robot implements ConstructableInUnitTest {

	public final Module target;
	public final int eta;
	public final int score;
	public final Map<Molecule, Integer> counts;
	public final Map<Molecule, Integer> expertise;

	public Robot(final String target, final int eta, final int score, final Map<Molecule, Integer> counts,
			final Map<Molecule, Integer> expertise) {
		this.target = Module.valueOf(target);
		this.eta = eta;
		this.score = score;
		this.counts = counts;
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
				Molecule.toMap(countA, countB, countC, countD, countE),
				Molecule.toMap(expertiseA, expertiseB, expertiseC, expertiseD, expertiseE));
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder();
		returned.append("new Robot(")
			.append(target.name()).append(",\t")
			.append(eta).append(",\t")
			.append(score).append(",\t")
			.append(Molecule.moleculeMapToArguments(counts)).append(", ")
			.append(Molecule.moleculeMapToArguments(expertise)).append(")");
		return returned;
	}

	public List<Molecule> findRequiredMoleculesFor(final Sample toProcess) {
		final List<Molecule> returned = new ArrayList<>();
		for(final Molecule type : Molecule.values()) {
			if(counts.get(type)<toProcess.cost.get(type)) {
				returned.add(type);
			}
		}
		return returned;
	}
}
