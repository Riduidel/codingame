package org.ndx.codingame.gaming;

public class Delay {
	public final long start = System.currentTimeMillis();
	public long howLong() {
		return System.currentTimeMillis()-start;
	}
	public boolean isElapsed(long delay) {
		return howLong()>delay;
	}
}
