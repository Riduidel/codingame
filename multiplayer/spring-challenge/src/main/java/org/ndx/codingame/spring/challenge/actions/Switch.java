package org.ndx.codingame.spring.challenge.actions;

import java.util.Arrays;
import java.util.List;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.entities.VirtualPac;

public class Switch extends AbstractAction implements PacAction {

	public final Type type;
	public Switch(AbstractPac pac, Type type) {
		super(pac);
		this.type = type;
	}
	@Override
	public String toCommandString() {
		return String.format("SWITCH %d %s %s", pac.id, type, message);
	}
	@Override
	public AbstractPac transform(ImmutablePlayground<Content> playground) {
		return new VirtualPac(pac.x, pac.y, 
				pac.id, pac.mine, 
				type, pac.speedTurnsLeft, EvolvableConstants.MAX_ABILITY_COOLDOWN,
				playground.get(pac));
	}
	@Override
	public String toString() {
		return "Switch [type=" + type + ", message=" + message + "]";
	}
	@Override
	public List<DiscretePoint> path() {
		return Arrays.asList(pac);
	}
}
