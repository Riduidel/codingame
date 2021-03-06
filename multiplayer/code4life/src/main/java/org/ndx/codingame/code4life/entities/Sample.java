package org.ndx.codingame.code4life.entities;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.ComparatorChain;
import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Sample implements ConstructableInUnitTest {
	public static class ByHealth implements Comparator<Sample> {
		@Override
		public int compare(final Sample o1, final Sample o2) {
			return (int) Math.signum(o1.health-o2.health);
		}
	}
	public static class ById implements Comparator<Sample> {
		@Override
		public int compare(final Sample o1, final Sample o2) {
			return (int) Math.signum(o1.id-o2.id);
		}
	}

	public static final Comparator<Sample> BY_ASCENDING_HEALTH = new ComparatorChain(new ByHealth(), new ById());
	public static final Comparator<Sample> BY_DESCENDING_HEALTH = Collections.reverseOrder(BY_ASCENDING_HEALTH);

	public final int id;
	public final int owner;
	/** Can be 1, 2, 3 */
	public final int rank;
	public Molecule expertiseGain;

	public final int health;
	public final Map<Molecule, Integer> cost;
	private Optional<Integer> adequation = Optional.empty();

	public Sample(final int sampleId, final int carriedBy, final int rank, final Molecule expertiseGain, final int health,
			final Map<Molecule, Integer> cost) {
		id = sampleId;
		owner = carriedBy;
		this.rank = rank;
		this.expertiseGain = expertiseGain;
		this.health = health;
		this.cost = cost;
	}
	public Sample(final int sampleId, final int carriedBy, final int rank, final String expertiseGain, final int health,
			final Map<Molecule, Integer> cost) {
		this(sampleId, carriedBy, rank, Molecule.nullableValueOf(expertiseGain), health, cost);
	}
	public Sample(final int sampleId, final int carriedBy, final int rank, final String expertiseGain, final int health,
			final int countA,
			final int countB,
			final int countC,
			final int countD,
			final int countE) {
		this(sampleId, carriedBy, rank, expertiseGain, health, MoleculeStore.toMap(countA, countB, countC, countD, countE));
	}


	public int getRank() {
		return rank;
	}

	public boolean isDiagnosed() {
		for(final Integer value : cost.values()) {
			if(value<0) {
				return false;
			} else {
				return true;
			}
		}
		return false;
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
		builder.append("]");
		return builder.toString();
	}
	public Sample redirectTo(final int i) {
		return new Sample(id, i, rank, expertiseGain, health, cost);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = id;
		result = prime * result;
		return result;
	}
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Sample other = (Sample) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	public int scienceProjectAdequation(final Playfield playfield) {
		if(!adequation.isPresent()) {
			int returned = 0;
			for(final Molecule m : playfield.completableProjectsRequirements()) {
				returned += cost.get(m);
			}
			adequation = Optional.of(returned);
		}
		return adequation.get();
	}
}
