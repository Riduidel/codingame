package org.ndx.codingame.code4life.entities;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ndx.codingame.code4life.Constants;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Sample implements ConstructableInUnitTest {

	public final int id;
	public final int owner;
	/** Can be 1, 2, 3 */
	public final int rank;
	public Molecule expertiseGain;

	public final int health;
	public final Map<Molecule, Integer> cost;
	private Optional<Integer> score = Optional.empty();
	public Sample(final int sampleId, final int carriedBy, final int rank, final String expertiseGain, final int health,
			final Map<Molecule, Integer> cost) {
		id = sampleId;
		owner = carriedBy;
		this.rank = rank;
		try {
			this.expertiseGain = Molecule.valueOf(expertiseGain);
		} catch(final Exception e) {
			this.expertiseGain = null;
		}
		this.health = health;
		this.cost = cost;
	}
	public Sample(final int sampleId, final int carriedBy, final int rank, final String expertiseGain, final int health,
			final int countA,
			final int countB,
			final int countC,
			final int countD,
			final int countE) {
		this(sampleId, carriedBy, rank, expertiseGain, health, MoleculeStore.toMap(countA, countB, countC, countD, countE));
	}


	public int getHealth() {
		return health;
	}

	public int getRank() {
		return rank;
	}

	public boolean isDiagnosed() {
		return cost.values().stream().collect(Collectors.summingInt((v) -> v))>0;
	}
	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder();
		returned.append("new Sample(")
			.append(id).append(",\t")
			.append(owner).append(",\t")
			.append(rank).append(",\t")
			.append("\"").append(expertiseGain).append("\"").append(",\t")
			.append(health).append(",\t")
			.append(MoleculeStore.moleculeMapToArguments(cost)).append(")");
		return returned;
	}
	/**
	 * Score interest of a sample.
	 * As of now, the following score components have been identified :
	 * <ul>
	 * <li>Cost of sample in molecules : the smaller, the better. (special case : if sample can't be processed, it's minus infinite</li>
	 * <li>health gain</li>
	 * <li>Correlation with science projects : if sample make us gain expertise for science project, a bonus multipler to health is added)</li>
	 * </ul>
	 * @param playfield
	 * @param my
	 * @return
	 */
	public int score(final Playfield playfield, final Robot my) {
		if(!score.isPresent()) {
			score = Optional.of(computeScore(playfield, my));
		}
		return score.get();
	}
	private int computeScore(final Playfield playfield, final Robot my) {
		int returned = 0;
		returned += computeCostScore(playfield, my);
		if(returned>Constants.SCORE_NOT_PROCESSABLE) {
			returned += health*interestForProjects(playfield);
		}
		return returned;
	}
	private int interestForProjects(final Playfield playfield) {
		if(playfield.completableProjectsRequirements().contains(expertiseGain)) {
			return Constants.SCORE_MULTIPLIER_USED_FOR_SCIENCE_PROJECTS;
		}
		return Constants.SCORE_MULTIPLIER_NOT_USED_FOR_SCIENCE_PROJECTS;
	}
	private int computeCostScore(final Playfield playfield, final Robot my) {
		final Map<Molecule, Integer> missingFromRobot = my.findMissingFor(this);
		final Map<Molecule, Integer> missingFromStore = playfield.findMissingFor(missingFromRobot);
		if(missingFromStore.isEmpty()) {
			return missingFromRobot.values().stream().collect(Collectors.summingInt((c) -> c));
		} else {
			return Constants.SCORE_NOT_PROCESSABLE;
		}
	}
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Sample [id=");
		builder.append(id);
		builder.append(", owner=");
		builder.append(owner);
		builder.append(", cost=");
		builder.append(cost);
		builder.append(", rank=");
		builder.append(rank);
		builder.append(", expertiseGain=");
		builder.append(expertiseGain);
		builder.append(", health=");
		builder.append(health);
		builder.append(", score=");
		builder.append(score);
		builder.append("]");
		return builder.toString();
	}
}
