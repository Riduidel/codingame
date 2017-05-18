package org.ndx.codingame.code4life.playground.states;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public abstract class StatedComputer {

	protected final Playfield playfield;

	public StatedComputer(final Playfield playfield) {
		this.playfield = playfield;
	}

	public abstract Action compute(Robot my);

	protected SortedSet<Sample> findInterestingSamplesIn(final Playfield playfield, final Robot my, final Collection<Sample> used) {
		final SortedSet<Sample> interesting = new TreeSet<>(Sample.BY_DESCENDING_HEALTH);
		for(final Sample s : used) {
			if(playfield.canProvideMoleculesFor(my, s)) {
				interesting.add(s);
			}
		}
		return interesting;
	}
}
