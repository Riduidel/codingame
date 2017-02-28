package org.ndx.codingame.ghostinthecell.actions;

public class Upgrade implements Action {

	private final int id;

	public Upgrade(final int id) {
		this.id = id;
	}

	@Override
	public String toCommandString() {
		return String.format("INC %d", id);
	}

}
