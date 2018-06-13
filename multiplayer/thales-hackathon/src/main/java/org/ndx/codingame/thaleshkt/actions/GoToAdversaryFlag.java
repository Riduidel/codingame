package org.ndx.codingame.thaleshkt.actions;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.thaleshkt.entities.UFO;
import org.ndx.codingame.thaleshkt.playground.Playfield;

public class GoToAdversaryFlag extends Move {

	public GoToAdversaryFlag(UFO moving, ContinuousPoint at, int i, boolean boost) {
		super(moving, at, i, boost);
	}

	@Override
	public Move resolveCollisions(Playfield p) {
		return avoidCollisions(p);
	}
}
