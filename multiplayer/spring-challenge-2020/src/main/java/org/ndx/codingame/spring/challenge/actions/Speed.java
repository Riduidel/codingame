package org.ndx.codingame.spring.challenge.actions;

import java.util.Arrays;
import java.util.List;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.VirtualPac;
import org.ndx.codingame.spring.challenge.playground.Playfield;

public class Speed extends AbstractAction {
	public Speed(AbstractPac pac) {
		super(pac);
	}
	@Override
	public String toCommandString() {
		return String.format("SPEED %d %s", pac.id, message);
	}
	@Override
	public AbstractPac transform(ImmutablePlayground<Content> playground) {
		return new VirtualPac(pac.x, pac.y, 
				pac.id, pac.mine, 
				pac.type, EvolvableConstants.MAX_SPEED_TURNS, 
				EvolvableConstants.MAX_ABILITY_COOLDOWN,
				playground.get(pac));
	}
	@Override
	public String toString() {
		return "Speed [message=" + message + "]";
	}
	@Override
	public List<DiscretePoint> path() {
		return Arrays.asList(pac);
	}
}
