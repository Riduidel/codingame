package org.ndx.codingame.code4life.entities;

import java.util.Map;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Sample implements ConstructableInUnitTest {

	public final int id;
	public final int owner;
	/** Can be 1, 2, 3 */
	public final int rank;
	public final String expertiseGain;

	public final int health;
	public final Map<Molecule, Integer> cost;
	public Sample(final int sampleId, final int carriedBy, final int rank, final String expertiseGain, final int health,
			final Map<Molecule, Integer> cost) {
		id = sampleId;
		owner = carriedBy;
		this.rank = rank;
		this.expertiseGain = expertiseGain;
		this.health = health;
		this.cost = cost;
	}
	public Sample(final int sampleId, final int carriedBy, final int rank, final String expertiseGain, final int health,
			final int countA,
			final int countB,
			final int countC,
			final int countD,
			final int countE) {
		this(sampleId, carriedBy, rank, expertiseGain, health, Molecule.toMap(countA, countB, countC, countD, countE));
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
			.append(Molecule.moleculeMapToArguments(cost)).append(")");
		return returned;
	}
	/**
	 * Compute cost in molecules for robot
	 * @param robot
	 * @return
	 */
	public int costFor(final Robot robot) {
		int computed = 0;
		for(final Molecule m : Molecule.values()) {
			computed += cost.get(m) - robot.expertise.get(m);
		}
		return computed;
	}
}
