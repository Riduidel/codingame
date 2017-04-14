package org.ndx.codingame.carribeancoders.actions;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.libgaming2d.MoveToPoint;

public class MoveTo extends MoveToPoint implements Action {

	public MoveTo(final AbstractPoint target) {
		super(target);
	}

	@Override
	public String toCommandString() {
		return "MOVE "+super.toCommandString();
	}
}
