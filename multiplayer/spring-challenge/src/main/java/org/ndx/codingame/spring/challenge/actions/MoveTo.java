package org.ndx.codingame.spring.challenge.actions;

import java.util.List;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.VirtualPac;
import org.ndx.codingame.spring.challenge.playground.Playfield;

public class MoveTo extends AbstractAction {
	
	public final List<DiscretePoint> path;

	public MoveTo(AbstractPac pac, List<DiscretePoint> destination) {
		super(pac);
		this.path = destination;
	}

	@Override
	public String toCommandString() {
		return String.format("MOVE %d %d %d %s", pac.id, destination().x, destination().y, message);
	}

	@Override
	public AbstractPac transform(ImmutablePlayground<Content> playground) {
		return new VirtualPac(destination().x, destination().y, 
				pac.id, pac.mine, 
				pac.type, 
				pac.speedTurnsLeft==0 ? 0 : pac.speedTurnsLeft-1, 
				pac.abilityCooldown==0 ? 0 : pac.abilityCooldown-1,
				playground.get(destination()));
	}

	@Override
	public String toString() {
		return "MoveTo [destination=" + destination() + ", message=" + message + "]";
	}

	public DiscretePoint destination() {
		return path.get(path.size()-1);
	}

	@Override
	public void update(Playfield playfield) {
		for(DiscretePoint p : path) {
			playfield.set(p, Ground.instance);
		}
	}
	
	@Override
	public List<DiscretePoint> path() {
		return path;
	}
}
