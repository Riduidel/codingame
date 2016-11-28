package org.ndx.codingame.fantastic.status;

public class TeamStatus implements StatusElement {
	public final int team;

	public TeamStatus(int id) { this.team = id; }

	@Override public void advanceOneTurn() { }
}