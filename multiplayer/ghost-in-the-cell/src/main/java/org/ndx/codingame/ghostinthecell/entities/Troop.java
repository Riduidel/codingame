package org.ndx.codingame.ghostinthecell.entities;

public class Troop extends Attack {

	public final int distance;

	public Troop(final int owner, final int count, final int distance) {
		super(owner, count);
		this.distance = distance;
	}

	public Troop advanceOneTurn() {
		return new Troop(owner, getCount(), distance-1);
	}

	@Override
	public String toString() {
		return String.format("Troop [owner=%s, count=%s, distance=%s]", owner, getCount(), distance);
	}

}
