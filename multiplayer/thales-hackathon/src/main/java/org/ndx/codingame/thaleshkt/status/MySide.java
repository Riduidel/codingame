package org.ndx.codingame.thaleshkt.status;

import org.ndx.codingame.libstatus.StatusElement;
import org.ndx.codingame.thaleshkt.playground.Side;

public class MySide implements StatusElement {

	public final Side my;

	public MySide(Side my) {
		this.my = my;
	}

	@Override
	public void advanceOneTurn() {
	}

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		StringBuilder returned = new StringBuilder();
		returned.append("new MySide(Side.").append(my.name()).append(")");
		return returned;
	}

}
