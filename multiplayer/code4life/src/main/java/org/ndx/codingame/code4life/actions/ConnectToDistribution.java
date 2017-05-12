package org.ndx.codingame.code4life.actions;

import org.ndx.codingame.code4life.entities.MoleculeType;
import org.ndx.codingame.gaming.actions.Action;

public class ConnectToDistribution implements Action {
	public final MoleculeType distributed;

	public ConnectToDistribution(final MoleculeType distributed) {
		super();
		this.distributed = distributed;
	}

	@Override
	public String toCommandString() {
		return String.format("CONNECT %s", distributed);
	}

}
