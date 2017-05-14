package org.ndx.codingame.code4life.entities;

import java.util.List;

import org.ndx.codingame.code4life.actions.Goto;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public enum Module {
	START_POS, SAMPLES, DIAGNOSIS, MOLECULES, LABORATORY;

	public Action computeMoveOf(final Robot my, final Playfield playfield) {
		final List<Sample> mySamples = playfield.getSamplesOf(my);
		switch(this) {
		case START_POS:
			return new Goto(Module.SAMPLES);
		default:
			return computeMoveOf(my, playfield, mySamples);
		}
	}

	public Action computeMoveOf(final Robot my, final Playfield playfield, final List<Sample> mySamples) {
		switch(this) {
		case DIAGNOSIS:
			return playfield.computeMoveOnDiagnosis(my, mySamples);
		case LABORATORY:
			return playfield.computeMoveOnLaboratory(my, mySamples);
		case MOLECULES:
			return playfield.computeMoveOnMolecules(my, mySamples);
		case SAMPLES:
			return playfield.computeMoveOnSamples(my, mySamples);
		default:
			return new Goto(Module.SAMPLES);
		}
	}
}
