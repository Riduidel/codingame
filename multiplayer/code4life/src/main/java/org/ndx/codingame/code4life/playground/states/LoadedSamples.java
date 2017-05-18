package org.ndx.codingame.code4life.playground.states;

import org.ndx.codingame.code4life.actions.Goto;
import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public class LoadedSamples extends StatedComputer {

	public LoadedSamples(Playfield playfield) {
		super(playfield);
	}

	@Override
	public Action compute(Robot my) {
		return new Goto(Module.DIAGNOSIS);
	}

}
