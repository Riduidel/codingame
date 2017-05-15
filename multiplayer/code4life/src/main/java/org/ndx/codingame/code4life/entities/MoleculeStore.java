package org.ndx.codingame.code4life.entities;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MoleculeStore {

	private Map<Molecule, Integer> available = new EnumMap<>(Molecule.class);

	public Map<Molecule, Integer> getAvailable() {
		return available;
	}

	public void addAllAvailable(final Map<Molecule, Integer> available) {
		this.available = available;
	}

	protected int getTotalCount() {
		int returned = 0;
		for(final Molecule type : Molecule.values()) {
			returned += getAvailable().get(type);
		}
		return returned;
	}
	public Map<Molecule, Integer> findMissingFor(final Sample sample) {
		return findMissingFor(sample.cost);
	}

	public Map<Molecule, Integer> findMissingFor(final Map<Molecule, Integer> cost) {
		return MoleculeStore.substract(cost, available);
	}

	public static StringBuilder moleculeMapToArguments(final Map<Molecule, Integer> map) {
		final StringBuilder returned = new StringBuilder();
		returned.append(map.get(Molecule.A)).append(", ");
		returned.append(map.get(Molecule.B)).append(", ");
		returned.append(map.get(Molecule.C)).append(", ");
		returned.append(map.get(Molecule.D)).append(", ");
		returned.append(map.get(Molecule.E));
		return returned;
	}

	public static Map<Molecule, Integer> toMap(final int countA, final int countB, final int countC, final int countD, final int countE) {
		final Map<Molecule, Integer> returned = new EnumMap<>(Molecule.class);
		returned.put(Molecule.A, countA);
		returned.put(Molecule.B, countB);
		returned.put(Molecule.C, countC);
		returned.put(Molecule.D, countD);
		returned.put(Molecule.E, countE);
		return returned;
	}

	public static Map<Molecule, Integer> substract(final Map<Molecule, Integer> cost,
			final Map<Molecule, Integer> available) {
		final Map<Molecule, Integer> missingRequirements = new EnumMap<>(Molecule.class);
		for(final Molecule type : Molecule.values()) {
			final int remaining = (cost.containsKey(type) ? cost.get(type) : 0) - available.get(type);
			if(remaining>0) {
				missingRequirements.put(type, remaining);
			}
		}
		return missingRequirements;
	}

	public static int totalCostOf(final Map<Molecule, Integer> cost) {
		return cost.values().stream().collect(Collectors.summingInt((c) -> c));
	}

}
