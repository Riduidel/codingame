package org.ndx.codingame.code4life.actions;

import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.gaming.actions.Action;

public class Goto implements Action {
	public final Module destination;

	public Goto(final Module destination) {
		super();
		this.destination = destination;
	}

	@Override
	public String toCommandString() {
		return String.format("GOTO %s", destination);
	}

}
