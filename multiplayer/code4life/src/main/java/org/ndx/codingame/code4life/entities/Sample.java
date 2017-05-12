package org.ndx.codingame.code4life.entities;

import java.util.Map;
import java.util.stream.Collectors;

public class Sample {

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

	public int getHealth() {
		return health;
	}

	public int getRank() {
		return rank;
	}

	public boolean isDiagnosed() {
		return cost.values().stream().collect(Collectors.summingInt((v) -> v))>0;
	}
}
