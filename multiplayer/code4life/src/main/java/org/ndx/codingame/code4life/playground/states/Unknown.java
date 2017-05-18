package org.ndx.codingame.code4life.playground.states;

import java.util.Optional;

import org.ndx.codingame.code4life.actions.ConnectToDiagnostic;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public class Unknown extends StatedComputer {

	public Unknown(final Playfield playfield) {
		super(playfield);
	}

	/**
	 * Diagnose first unknown sample
	 */
	@Override
	public Action compute(final Robot my) {
		final Optional<Sample> undiagnosed = playfield.getFirstUndiagnosedSample(my);
		if(undiagnosed.isPresent()) {
			return new ConnectToDiagnostic(undiagnosed.get());
		} else {
			throw new UnsupportedOperationException("No undiagnosed sample ?");
		}
	}

}
