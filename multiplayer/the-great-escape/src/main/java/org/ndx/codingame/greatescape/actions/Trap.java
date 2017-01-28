package org.ndx.codingame.greatescape.actions;

import org.ndx.codingame.greatescape.entities.Orientation;
import org.ndx.codingame.greatescape.entities.Wall;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class Trap implements Action {

	private final DiscretePoint position;
	private final Orientation orientation;

	public Trap(final Orientation v, final DiscretePoint move) {
		orientation = v;
		position = move;
	}

	@Override
	public String toString() {
		return String.format("%d %d %s", position.x, position.y, orientation);
	}

	public boolean canInstall(final Playfield tested) {
		return !Wall.class.isInstance(tested.get(tested.toPlayfieldPositionForWall(position.x, position.y, orientation)));
	}
}
