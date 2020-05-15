package org.ndx.codingame.spring.challenge.actions;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.VirtualPac;

public class MoveTo extends AbstractAction {
	
	public final DiscretePoint destination;

	public MoveTo(AbstractPac pac, DiscretePoint destination) {
		super(pac);
		this.destination = destination;
	}

	@Override
	public String toCommandString() {
		return String.format("MOVE %d %d %d %s", pac.id, destination.x, destination.y, message);
	}

	@Override
	public AbstractPac transform(ImmutablePlayground<Content> playground) {
		return new VirtualPac(destination.x, destination.y, 
				pac.id, pac.mine, 
				pac.type, 
				pac.speedTurnsLeft==0 ? 0 : pac.speedTurnsLeft-1, 
				pac.abilityCooldown==0 ? 0 : pac.abilityCooldown-1,
				playground.get(destination));
	}

	@Override
	public String toString() {
		return "MoveTo [destination=" + destination + ", message=" + message + "]";
	}

}
