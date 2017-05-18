package org.ndx.codingame.code4life.playground.states;

import org.ndx.codingame.code4life.actions.ConnectToDiagnostic;
import org.ndx.codingame.code4life.actions.Goto;
import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public class Known extends StatedComputer {

	public Known(final Playfield playfield) {
		super(playfield);
	}

	@Override
	public Action compute(final Robot my) {
		if (playfield.canService(my)) {
			return new Goto(Module.MOLECULES);
		} else if(my.target.equals(Module.DIAGNOSIS)){
			final Sample toRemove = playfield.findUnservicable(my).get(0);
			return new ConnectToDiagnostic(toRemove);
		} else {
			return new Goto(Module.DIAGNOSIS);
		}
	}
}
