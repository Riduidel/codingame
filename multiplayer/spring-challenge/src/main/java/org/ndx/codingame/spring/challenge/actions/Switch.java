package org.ndx.codingame.spring.challenge.actions;

import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.Type;

public class Switch extends AbstractAction implements PacAction {

	public final Type type;
	public Switch(Pac pac, Type type) {
		super(pac);
		this.type = type;
	}
	@Override
	public String toCommandString() {
		return String.format("SWITCH %d %s %s", pac.id, type, message);
	}
	@Override
	public Pac transform() {
		return new Pac(pac.x, pac.y, 
				pac.id, pac.mine, 
				type, pac.speedTurnsLeft, EvolvableConstants.MAX_ABILITY_COOLDOWN);
	}
	@Override
	public String toString() {
		return "Switch [type=" + type + "]";
	}

}
