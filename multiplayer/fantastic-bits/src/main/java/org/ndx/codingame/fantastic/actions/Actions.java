package org.ndx.codingame.fantastic.actions;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Actions {

	public static String moveTo(final Entity nearest) {
		return moveTo(nearest.position, Constants.WIZARD_MAX_SPEED);
	}
	public static String moveTo(final ContinuousPoint nearest, int speed) {
		return String.format("MOVE %d %d %d", (int) nearest.x, (int) nearest.y, Constants.WIZARD_MAX_SPEED);
	}

	public static String throwTo(final ContinuousPoint goal) {
		return String.format("THROW %d %d %d", (int) goal.x, (int) goal.y, Constants.MAX_THROW_POWER);
	}

}
