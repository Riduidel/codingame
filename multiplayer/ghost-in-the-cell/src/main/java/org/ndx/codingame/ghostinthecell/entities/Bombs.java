package org.ndx.codingame.ghostinthecell.entities;

public class Bombs {

	private int count;

	public Bombs(final int i) {
		count = i;
	}

	public int getCount() {
		return count;
	}

	public void setCount(final int count) {
		this.count = count;
	}

	public boolean canBomb() {
		return count>0;
	}

	public void dropOne() {
		count--;
	}

}
