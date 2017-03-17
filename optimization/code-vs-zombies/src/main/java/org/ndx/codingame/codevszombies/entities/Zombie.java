package org.ndx.codingame.codevszombies.entities;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Vector;

public class Zombie implements Entity {
	public static final int SPEED = 400;

	private final int id;
	private final Vector move;

	public Zombie(final int zombieId, final int zombieX, final int zombieY, final int zombieXNext, final int zombieYNext) {
		this(zombieId,
				new Vector(new ContinuousPoint(zombieX, zombieYNext), new ContinuousPoint(zombieXNext, zombieYNext)));
	}
	
	public Zombie(final int zombieId, final Vector move) {
		id = zombieId;
		this.move = move;
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder();
		returned.append("new Zombie(").append(id).append(", ")
			.append((int)move.first.x).append(", ").append((int)move.first.y).append(", ")
			.append((int)move.second.x).append(", ").append((int)move.second.y)
			.append(")");
		return returned;
	}
	@Override
	public ContinuousPoint getPosition() {
		return move.first;
	}

	public int getId() {
		return id;
	}

	public Vector getMove() {
		return move;
	}

	@Override
	public String toString() {
		return String.format("%s [id=%d, position=%s]", getClass().getSimpleName(), getId(), getPosition());
	}
}
