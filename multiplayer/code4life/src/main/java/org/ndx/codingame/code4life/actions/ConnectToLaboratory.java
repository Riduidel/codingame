package org.ndx.codingame.code4life.actions;

import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.gaming.actions.Action;

public class ConnectToLaboratory implements Action {
	public final int id;

	public ConnectToLaboratory(final int id) {
		super();
		this.id = id;
	}

	public ConnectToLaboratory(final Sample toProcess) {
		this(toProcess.id);
	}

	@Override
	public String toCommandString() {
		return String.format("CONNECT %d", id);
	}

}
