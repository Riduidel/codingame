package org.ndx.codingame.spring.challenge.actions;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.entities.Pac;

public class MoveTo extends AbstractAction {
	
	public final DiscretePoint destination;

	public MoveTo(Pac pac, DiscretePoint destination) {
		super(pac);
		this.destination = destination;
	}

	@Override
	public String toCommandString() {
		return String.format("MOVE %d %d %d %s", pac.id, destination.x, destination.y, message);
	}

	@Override
	public Pac transform() {
		return new Pac(destination.x, destination.y, 
				pac.id, pac.mine, 
				pac.type, pac.speedTurnsLeft, pac.abilityCooldown);
	}

	@Override
	public String toString() {
		return "MoveTo [" + destination + "]";
	}

}
