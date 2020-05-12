package org.ndx.codingame.spring.challenge.actions;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.Pac;

public class Speed extends AbstractAction {
	public Speed(Pac pac) {
		super(pac);
	}
	@Override
	public String toCommandString() {
		return String.format("SPEED %d %s", pac.id, message);
	}
	@Override
	public Pac transform() {
		return new Pac(pac.x, pac.y, 
				pac.id, pac.mine, 
				pac.type, EvolvableConstants.MAX_SPEED_TURNS, EvolvableConstants.MAX_ABILITY_COOLDOWN);
	}
	@Override
	public String toString() {
		return "Speed []";
	}
}
