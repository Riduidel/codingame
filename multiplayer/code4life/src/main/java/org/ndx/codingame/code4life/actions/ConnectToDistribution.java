package org.ndx.codingame.code4life.actions;

import org.ndx.codingame.code4life.entities.Molecule;
import org.ndx.codingame.gaming.actions.Action;

public class ConnectToDistribution implements Action {
	public final Molecule distributed;

	public ConnectToDistribution(final Molecule distributed) {
		super();
		this.distributed = distributed;
	}

	@Override
	public String toCommandString() {
		return String.format("CONNECT %s", distributed);
	}

}
