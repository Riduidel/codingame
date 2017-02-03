package org.ndx.codingame.greatescape.actions;

import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class MoveTo extends AbstractAction implements Action {
	public final ScoredDirection<?> destination;

	public MoveTo(final ScoredDirection<?> destination) {
		super();
		this.destination = destination;
	}
	
	@Override
	public String toCodingame() {
		return String.format("%s %s", destination.name, message);
	}
}
