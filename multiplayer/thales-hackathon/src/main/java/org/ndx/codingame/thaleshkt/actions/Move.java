package org.ndx.codingame.thaleshkt.actions;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.thaleshkt.entities.UFO;
import org.ndx.codingame.thaleshkt.playground.Playfield;

public class Move {
	public final UFO moving;
	public final ContinuousPoint destination;
	public final int thrust;
	public final boolean boost;
	

	public Move(UFO moving, ContinuousPoint at, int i, boolean boost) {
		this.moving = moving;
		this.destination = at;
		this.thrust = i;
		this.boost = boost;
	}
	@Override
	public String toString() {
		if(boost) {
			return String.format("%d %d BOOST", (int) destination.x, (int) destination.y);
		} else {
			return String.format("%d %d %d", (int) destination.x, (int) destination.y, thrust);
		}
	}
	public void resolveCollisions(Playfield p) {}
}
