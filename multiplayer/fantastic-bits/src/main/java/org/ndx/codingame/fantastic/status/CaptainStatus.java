package org.ndx.codingame.fantastic.status;

public class CaptainStatus implements StatusElement {
	public final int captain;

	public CaptainStatus(int id) { this.captain = id; }

	@Override public void advanceOneTurn() { }
}