package org.ndx.codingame.ghostinthecell.actions;

public class MoveTo implements Action {

	public final int source;
	public final int destination;
	public final int count;

	public MoveTo(final int source, final int destination, final int count) {
		this.source = source;
		this.destination = destination;
		this.count = count;
	}

	@Override
	public String toCommandString() {
		return String.format("MOVE %d %d %d", source, destination, count);
	}
}
