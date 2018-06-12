package org.ndx.codingame.thaleshkt.status;

import org.ndx.codingame.libstatus.StatusElement;
import org.ndx.codingame.thaleshkt.actions.Move;

public class CanBoost implements StatusElement {
	public class CanBoostFor {
		public CanBoostFor(int d) {
			this.delay = d;
		}
		private static final int DELAY = 8;
		private int delay;
		public void advanceOneTurn() {
			delay = Math.max(delay-1, 0);
		}
		public boolean canBoost() {
			return delay<=0;
		}
		public void update(Move move) {
			if(move.boost) {
				delay= DELAY;
			}
		}
	}
	public CanBoostFor first;
	public CanBoostFor second;

	public CanBoost() {
		this(0, 0);
	}
	
	public CanBoost(int first, int second) {
		this.first = new CanBoostFor(first);
		this.second = new CanBoostFor(second);
	}

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		StringBuilder returned = new StringBuilder();
		returned.append("new CanBoost(").append(first.delay).append(", ").append(second.delay).append(")");
		return returned;
	}

	@Override
	public void advanceOneTurn() {
		first.advanceOneTurn();
		second.advanceOneTurn();
	}

	public void update(Move returned) {
		switch(returned.moving.gamer) {
		case FIRST:
			first.update(returned);
			break;
		case SECOND:
			second.update(returned);
			break;
		}
	}
}
