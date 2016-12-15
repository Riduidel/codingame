package org.ndx.codingame.fantastic.status;

import org.ndx.codingame.fantastic.Playground;
import org.ndx.codingame.lib2d.shapes.Segment;

public class TeamStatus implements StatusElement {
	public final int team;

	public TeamStatus(final int id) { team = id; }

	@Override public void advanceOneTurn() { }

	public Segment getAttacked() {
		return Playground.goals.get(1-team);
	}

	public Segment getDefended() {
		return Playground.goals.get(team);
	}
}