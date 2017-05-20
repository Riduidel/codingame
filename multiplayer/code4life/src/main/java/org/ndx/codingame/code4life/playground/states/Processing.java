package org.ndx.codingame.code4life.playground.states;

import java.util.ArrayList;
import java.util.List;

import org.ndx.codingame.code4life.actions.ConnectToLaboratory;
import org.ndx.codingame.code4life.actions.Goto;
import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public class Processing extends StatedComputer {

	public Processing(final Playfield playfield) {
		super(playfield);
	}

	public List<Sample> samplesWithMoleculesSetTo(final Robot my, final boolean expected) {
		final List<Sample> returned = new ArrayList<>();
		for (final Sample s : playfield.getSamplesListOf(my)) {
			if (Playfield.canProvideMoleculesFor(my.findMissingFor(s)) == expected) {
				returned.add(s);
			}
		}
		return returned;
	}

	@Override
	public Action compute(final Robot my) {
		final List<Sample> processableSamples = samplesWithMoleculesSetTo(my, true);
		final boolean hasSamples = !playfield.getSamplesListOf(my).isEmpty();
		if (processableSamples.isEmpty()) {
			if (hasSamples) {
				final Playfield derived = playfield.derive(Module.DIAGNOSIS.distanceTo(Module.MOLECULES));
				if (!findInterestingSamplesIn(derived, my, playfield.getSamplesListOf(my)).isEmpty()) {
					return new Goto(Module.MOLECULES);
				}
			}
			final Playfield derived = playfield
					.derive(my.target.distanceTo(Module.DIAGNOSIS) + Module.DIAGNOSIS.distanceTo(Module.MOLECULES));
			if (!findInterestingSamplesIn(derived, my, playfield.getSamplesListOf(playfield)).isEmpty()) {
				return new Goto(Module.DIAGNOSIS);
			}
			return new Goto(Module.SAMPLES);
		} else {
			processableSamples.sort(interestComparator);
			return new ConnectToLaboratory(processableSamples.get(0));
		}
	}
}