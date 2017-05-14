package org.ndx.codingame.code4life.entities;

import java.util.EnumMap;
import java.util.Map;

public enum Molecule {
	A, B, C, D, E;

	public static Map<Molecule, Integer> toMap(final int countA, final int countB, final int countC, final int countD, final int countE) {
		final Map<Molecule, Integer> returned = new EnumMap<>(Molecule.class);
		returned.put(A, countA);
		returned.put(B, countB);
		returned.put(C, countC);
		returned.put(D, countD);
		returned.put(E, countE);
		return returned;
	}

	public static StringBuilder moleculeMapToArguments(final Map<Molecule, Integer> map) {
		final StringBuilder returned = new StringBuilder();
		returned.append(map.get(A)).append(", ");
		returned.append(map.get(B)).append(", ");
		returned.append(map.get(C)).append(", ");
		returned.append(map.get(D)).append(", ");
		returned.append(map.get(E));
		return returned;
	}
}
