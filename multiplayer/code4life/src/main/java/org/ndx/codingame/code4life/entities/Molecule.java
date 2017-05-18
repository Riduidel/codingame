package org.ndx.codingame.code4life.entities;

public enum Molecule {
	A, B, C, D, E;

	public static Molecule nullableValueOf(final String expertiseGain) {
		try {
			return valueOf(expertiseGain);
		} catch(final Exception e) {
			return null;
		}
	}
}
