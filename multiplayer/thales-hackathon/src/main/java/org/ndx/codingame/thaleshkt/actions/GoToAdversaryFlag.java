package org.ndx.codingame.thaleshkt.actions;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.thaleshkt.entities.UFO;

public class GoToAdversaryFlag extends Move {

	public GoToAdversaryFlag(UFO moving, ContinuousPoint at, int i, boolean boost) {
		super(moving, at, i, boost);
	}

}
