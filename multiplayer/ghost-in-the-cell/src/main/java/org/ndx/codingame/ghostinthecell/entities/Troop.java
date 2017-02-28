package org.ndx.codingame.ghostinthecell.entities;

public class Troop {

	public final int owner;
	public final int count;
	public final int distance;

	public Troop(final int owner, final int count, final int distance) {
		this.owner = owner;
		this.count = count;
		this.distance = distance;
	}

	public Troop advanceOneTurn() {
		return new Troop(owner, count, distance-1);
	}

	@Override
	public String toString() {
		return String.format("Troop [owner=%s, count=%s, distance=%s]", owner, count, distance);
	}

}
