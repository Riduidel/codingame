package org.ndx.codingame.thaleshkt.actions;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.thaleshkt.entities.UFO;

public class DefendFlag extends Move {

	public DefendFlag(UFO moving, ContinuousPoint at, int i, boolean boost) {
		super(moving, at, i, boost);
	}

}
