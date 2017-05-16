package org.ndx.codingame.code4life.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

	public List<Molecule> findRequiredMoleculesFor(final Sample toProcess) {
		final List<Molecule> returned = new ArrayList<>();
		for(final Molecule type : Molecule.values()) {
			if(getAvailable().get(type)<toProcess.cost.get(type)) {
				returned.add(type);
			}
		}
		return returned;
	}
	/**
	 * The best sample is the one which
	 * <ul><li>Do not cost more than 10 molecules</li>
	 * <li>Gives the most health</li>
	 * </ul>
	 * @param samplesInCloud
	 * @return
	 */
	public Optional<Sample> findBestSampleIn(final List<Sample> samplesInCloud) {
		return samplesInCloud.stream()
			.filter((s) -> costOf(s)<Constants.MAX_MOLECULES)
			.sorted(Comparator.comparingInt(Sample::getHealth))
			.findFirst();
	}

	public int costOf(final Sample s) {
		final Map<Molecule, Integer> expertiseApplied = MoleculeStore.substract(s.cost, expertise);
		return totalCostOf(expertiseApplied);
	}
	public boolean canSendToLaboratory(final Sample sample) {
		return findMissingFor(sample).isEmpty();
	}

	@Override
	public Map<Molecule, Integer> findMissingFor(final Map<Molecule, Integer> cost) {
		final Map<Molecule, Integer> expertiseApplied = MoleculeStore.substract(cost, expertise);
		if(expertiseApplied.isEmpty()) {
			return expertiseApplied;
		}
		return super.findMissingFor(expertiseApplied);
	}

	public boolean isFull() {
		return getTotalCount()>=Constants.MAX_MOLECULES;
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
}
