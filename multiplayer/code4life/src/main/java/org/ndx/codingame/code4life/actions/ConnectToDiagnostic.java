package org.ndx.codingame.code4life.actions;

import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.gaming.actions.Action;

public class ConnectToDiagnostic implements Action {
	public final int id;

	public ConnectToDiagnostic(final int id) {
		super();
		this.id = id;
	}

	public ConnectToDiagnostic(final Sample toCollect) {
		this(toCollect.id);
	}

	@Override
	public String toCommandString() {
		return String.format("CONNECT %d", id);
	}

}
