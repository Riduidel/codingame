package org.ndx.codingame.gaming.actions;

public abstract class AbstractAction implements Action {
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Action) {
			final Action other = (Action) obj;
			return toCommandString().equals(other.toCommandString());
		} else {
			return false;
		}
	}
}
