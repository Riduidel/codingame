package org.ndx.codingame.thaleshkt.entities;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Circle;

/** Base class of my entities */
public abstract class AbstractEntity {
	public final Circle position;

	public AbstractEntity(int x, int y, int radius) {
		this.position = new Circle(new ContinuousPoint(x, y), radius);
	}

	public abstract <Type> Type accept(EntityVisitor<Type> visitor);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

}
