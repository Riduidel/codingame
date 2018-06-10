package org.ndx.codingame.fantastic.status;

import org.ndx.codingame.libstatus.StatusElement;

public class CaptainStatus implements StatusElement {
	public final int captain;

	public CaptainStatus(int id) { this.captain = id; }

	@Override public void advanceOneTurn() { }

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		StringBuilder returned = new StringBuilder();
		returned.append("new CaptainStatus(").append(captain).append(")");
		return returned;
	}
}