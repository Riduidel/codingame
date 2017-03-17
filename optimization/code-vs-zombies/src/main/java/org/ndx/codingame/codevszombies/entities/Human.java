package org.ndx.codingame.codevszombies.entities;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Human implements Entity {

	private final int id;
	private final ContinuousPoint position;

	public Human(final int humanId, final int humanX, final int humanY) {
		this(humanId, new ContinuousPoint(humanX, humanY));
	}


	public Human(final int id, final ContinuousPoint position) {
		this.id = id;
		this.position = position;
	}


	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder();
		returned.append("new Human(").append(id).append(", ").append((int)position.x).append(", ").append((int)position.y).append(")");
		return returned;
	}

	@Override
	public ContinuousPoint getPosition() {
		return position;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Human other = (Human) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}


	public int getId() {
		return id;
	}

	public Human advanceOneTurn() {
		return new Human(id, position);
	}

	@Override
	public String toString() {
		return String.format("%s [id=%d, position=%s]", getClass().getSimpleName(), getId(), getPosition());
	}
}
