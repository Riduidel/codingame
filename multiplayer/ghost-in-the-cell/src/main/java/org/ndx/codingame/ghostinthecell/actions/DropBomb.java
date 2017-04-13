package org.ndx.codingame.ghostinthecell.actions;

import org.ndx.codingame.gaming.actions.Action;

public class DropBomb implements Action {

	private final int destination;
	private final int source;

	public DropBomb(final int source, final int destination) {
		this.source = source;
		this.destination = destination;
	}

	@Override
	public String toCommandString() {
		return String.format("BOMB %d %d", source, destination);
	}

}
