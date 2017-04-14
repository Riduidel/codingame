package org.ndx.codingame.carribeancoders.actions;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.libgaming2d.MoveToPoint;

public class ShootAt extends MoveToPoint implements Action {

	public ShootAt(final AbstractPoint target) {
		super(target);
	}

	@Override
	public String toCommandString() {
		return "FIRE " + super.toCommandString();
	}
}
