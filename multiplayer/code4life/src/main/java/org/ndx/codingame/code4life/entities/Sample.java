package org.ndx.codingame.code4life.entities;

import java.util.Map;

public class Sample {

	public final int id;
	public final int owner;
	public final int rank;
	public final String expertiseGain;
	public final int health;
	public final Map<MoleculeType, Integer> cost;

	public Sample(final int sampleId, final int carriedBy, final int rank, final String expertiseGain, final int health,
			final Map<MoleculeType, Integer> cost) {
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

}
