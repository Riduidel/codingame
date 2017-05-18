package org.ndx.codingame.code4life.entities;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.ndx.codingame.gaming.MapUtils;

public class MoleculeStore {

	public static StringBuilder moleculeMapToArguments(final Map<Molecule, Integer> map) {
		final StringBuilder returned = new StringBuilder();
		returned.append(map.get(Molecule.A)).append(", ");
		returned.append(map.get(Molecule.B)).append(", ");
		returned.append(map.get(Molecule.C)).append(", ");
		returned.append(map.get(Molecule.D)).append(", ");
		returned.append(map.get(Molecule.E));
		return returned;
	}

	public static Map<Molecule, Integer> substract(final Map<Molecule, Integer> first,
			final Map<Molecule, Integer> second) {
		final Map<Molecule, Integer> missingRequirements = new EnumMap<>(Molecule.class);
		for(final Molecule type : Molecule.values()) {
			final int remaining =
					MapUtils.safeGet(first, type, 0)
					-
					MapUtils.safeGet(second, type, 0);
			missingRequirements.put(type, remaining);
		}
		return missingRequirements;
	}

	public static Map<Molecule, Integer> add(final Map<Molecule, Integer> first,
			final Map<Molecule, Integer> second) {
		final Map<Molecule, Integer> returned = new EnumMap<>(Molecule.class);
		for(final Molecule type : Molecule.values()) {
			returned.put(type, first.get(type)+second.get(type));
		}
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

	public static int totalCostOf(final Map<Molecule, Integer> cost) {
		int returned = 0;
		for(final Integer i : cost.values()) {
			returned+=i;
		}
		return returned;
	}
	private Map<Molecule, Integer> available = new EnumMap<>(Molecule.class);

	public void addAllAvailable(final Map<Molecule, Integer> available) {
		this.available = new EnumMap<>(Molecule.class);
		this.available.putAll(available);
	}

	public Map<Molecule, Integer> findMissingFor(final Map<Molecule, Integer> cost) {
		return MoleculeStore.substract(cost, available);
	}

	public Map<Molecule, Integer> findMissingFor(final Sample sample) {
		return findMissingFor(sample.cost);
	}

	public Map<Molecule, Integer> getAvailable() {
		return available;
	}

	/**
	 * @return molecule which has the least number
	 */
	public Molecule getRarestMolecule() {
		return MapUtils.keyWithSmallestValue(available).get();
	}

	public int getTotalCount() {
		int returned = 0;
		for(final int value : getAvailable().values()) {
			returned += value;
		}
		return returned;
	}

	public Collection<Molecule> moleculesByRarity() {
		return MapUtils.keysByValue(available);
	}
}
