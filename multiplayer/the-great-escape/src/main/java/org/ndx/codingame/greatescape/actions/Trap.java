package org.ndx.codingame.greatescape.actions;

import java.util.Comparator;

import org.ndx.codingame.greatescape.entities.Orientation;
import org.ndx.codingame.greatescape.entities.Wall;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class Trap extends AbstractAction implements Comparable<Trap>{

	private DiscretePoint position;
	private final Orientation orientation;
	private int score;
	
	private static Comparator<Trap> comparator = Comparator.comparing(Trap::getScore).reversed();

	public Trap(final Orientation v, final DiscretePoint move) {
		orientation = v;
		position = move;
	}


	@Override
	public String toString() {
		return String.format("[%d,%d %s / %d]", position.x, position.y, orientation, score);
	}

	@Override
	public String toCodingame() {
		return String.format("%d %d %s %s", position.x, position.y, orientation, message);
	}

	public boolean canInstall(final Playfield tested) {
		return !Wall.class.isInstance(tested.get(tested.toPlayfieldPositionForWall(position.x, position.y, orientation)));
	}

	public void addScore(final int localScore) {
		score += localScore;
	}

	@Override
	public int compareTo(final Trap o) {
		return comparator.compare(this, o);
	}

	public int getScore() {
		return score;
	}

	/**
	 * Try to put trap where there is room
	 * @param tested
	 * @return
	 */
	public boolean putSomewhereValid(final Playfield tested) {
		DiscretePoint playfielPos = tested.toPlayfieldPositionForWall(position.x, position.y, orientation);
		if(!tested.contains(playfielPos)) {
			return false;
		}
		if(Wall.class.isInstance(tested.get(playfielPos))) {
			return false;
		}
		switch (orientation) {
		case H:
			playfielPos = tested.toPlayfieldPositionForWall(position.x+1, position.y, orientation);
			if(Wall.class.isInstance(tested.get(playfielPos))) {
				// Let's try to move the trap away
				if(position.x>0) {
					playfielPos = tested.toPlayfieldPositionForWall(position.x-1, position.y, orientation);
					if(!Wall.class.isInstance(tested.get(playfielPos))) {
						position = new DiscretePoint(position.x-1, position.y);
						return true;
					} else {
						return false;
					}
				}
			}
			break;
		case V:
			playfielPos = tested.toPlayfieldPositionForWall(position.x, position.y+1, orientation);
			if(Wall.class.isInstance(tested.get(playfielPos))) {
				// Let's try to move the trap away
				if(position.x>0) {
					playfielPos = tested.toPlayfieldPositionForWall(position.x, position.y-1, orientation);
					if(!Wall.class.isInstance(tested.get(playfielPos))) {
						position = new DiscretePoint(position.x, position.y-1);
						return true;
					} else {
						return false;
					}
				}
			}
			break;

		default:
			break;
		}
		return true;
	}
}
