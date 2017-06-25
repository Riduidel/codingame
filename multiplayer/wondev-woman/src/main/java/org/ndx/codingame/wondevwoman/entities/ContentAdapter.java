package org.ndx.codingame.wondevwoman.entities;

public abstract class ContentAdapter<Returned> implements ContentVisitor<Returned> {
	protected Returned defaultValue;

	protected ContentAdapter() {
	}

	protected ContentAdapter(final Returned value) {
		this.defaultValue = value;
	}
	@Override
	public Returned visitFloor(final Floor floor) {
		return defaultValue;
	}

	@Override
	public Returned visitHole(final Hole hole) {
		return defaultValue;
	}
}
