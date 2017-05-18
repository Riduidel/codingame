package org.ndx.codingame.code4life.playground.states;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.ComparatorChain;
import org.ndx.codingame.gaming.actions.Action;

public abstract class StatedComputer {
	protected static class WithScience implements Comparator<Sample> {
		private final Playfield playfield;

		public WithScience(final Playfield playfield) {
			super();
			this.playfield = playfield;
		}

		@Override
		public int compare(final Sample o1, final Sample o2) {
			return (int) Math.signum(o1.scienceProjectAdequation(playfield)-o2.scienceProjectAdequation(playfield));
		}

	}

	protected final Playfield playfield;
	private final Comparator<Sample> interestComparator;

	public StatedComputer(final Playfield playfield) {
		this.playfield = playfield;
		interestComparator = new ComparatorChain<>(Collections.reverseOrder(new WithScience(playfield)), Sample.BY_DESCENDING_HEALTH);
	}

	public abstract Action compute(Robot my);

	protected SortedSet<Sample> findInterestingSamplesIn(final Playfield playfield, final Robot my, final Collection<Sample> used) {
		final SortedSet<Sample> interesting = new TreeSet<>(interestComparator);
		for(final Sample s : used) {
			if(playfield.canProvideMoleculesFor(my, s)) {
				interesting.add(s);
			}
		}
		return interesting;
	}
}
