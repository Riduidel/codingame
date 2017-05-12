package org.ndx.codingame.code4life.actions;

import org.ndx.codingame.gaming.actions.Action;

public class ConnectToSampler implements Action {
	public final int rank;

	public ConnectToSampler(final int rank) {
		super();
		this.rank = rank;
	}

	@Override
	public String toCommandString() {
		return String.format("CONNECT %d", rank);
	}

}
