package org.ndx.codingame.carribeancoders.actions;

import org.ndx.codingame.gaming.actions.AbstractAction;
import org.ndx.codingame.gaming.actions.Action;

public class Slower extends AbstractAction implements Action {

	@Override
	public String toCommandString() {
		return "SLOWER";
	}

}
