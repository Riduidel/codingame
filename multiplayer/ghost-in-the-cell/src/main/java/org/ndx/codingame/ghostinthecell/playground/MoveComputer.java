package org.ndx.codingame.ghostinthecell.playground;

import java.util.Collection;

import org.ndx.codingame.gaming.actions.Action;

@FunctionalInterface
public interface MoveComputer {
	public Collection<Action> compute();

}
