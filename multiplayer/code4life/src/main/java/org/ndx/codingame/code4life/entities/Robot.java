package org.ndx.codingame.code4life.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ndx.codingame.code4life.Constants;
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
			.append("\"").append(target.name()).append("\"").append(",\t")
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
			.filter((s) -> s.costFor(this)<Constants.MAX_MOLECULES)
			.sorted(Comparator.comparingInt(Sample::getHealth))
			.findFirst();
	}
	public boolean canSendToLaboratory(final Sample sample) {
		return findMissingFor(sample).isEmpty();
	}
	public Map<Molecule, Integer> findMissingFor(final Sample sample) {
		final Map<Molecule, Integer> missingRequirements = new EnumMap<>(Molecule.class);
		for(final Molecule type : Molecule.values()) {
			final int remaining = sample.cost.get(type) - (counts.get(type)+expertise.get(type));
			if(remaining>0) {
				missingRequirements.put(type, remaining);
			}
		}
		return missingRequirements;
	}
	public boolean isFull() {
		return getTotalCount()>=Constants.MAX_MOLECULES;
	}
	private int getTotalCount() {
		int returned = 0;
		for(final Molecule type : Molecule.values()) {
			returned += counts.get(type);
		}
		return returned;
	}
}
