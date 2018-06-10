package org.ndx.codingame.thaleshkt.actions;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Move {
	public Move(ContinuousPoint at, int i, boolean boost) {
		this.destination = at;
		this.thrust = i;
		this.boost = boost;
	}
	public final ContinuousPoint destination;
	public final int thrust;
	public final boolean boost;
	
	@Override
	public String toString() {
		if(boost) {
			return String.format("%d %d BOOST", (int) destination.x, (int) destination.y);
		} else {
			return String.format("%d %d BOOST", (int) destination.x, (int) destination.y, thrust);
		}
	}
}
