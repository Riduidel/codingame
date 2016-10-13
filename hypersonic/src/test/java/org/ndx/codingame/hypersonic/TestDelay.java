package org.ndx.codingame.hypersonic;

import org.ndx.codingame.gaming.Delay;

public class TestDelay extends Delay {
	private int count, start;

	public TestDelay(int count) {
		super();
		this.start = this.count = count;
	}
	@Override
	public boolean isElapsed(long delay) {
		return count--<0;
	}
	@Override
	public long howLong() {
		return start-count;
	}
}