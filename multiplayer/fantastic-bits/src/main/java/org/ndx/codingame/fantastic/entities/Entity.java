package org.ndx.codingame.fantastic.entities;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Circle;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.lib2d.shapes.Vector;

public abstract class Entity {
	public static class ByDistanceTo implements Comparator<Entity> {

		private final AbstractPoint.PositionByDistance2To center;

		public ByDistanceTo(final Entity principal) {
			center = new AbstractPoint.PositionByDistance2To(principal.position);
		}

		@Override
		public int compare(final Entity o1, final Entity o2) {
			return center.compare(o1.position, o2.position);
		}
	}

	public final ContinuousPoint position;
	public final ContinuousPoint speed;
	public final Vector direction;
	public final int id;
	private final Map<Double, Circle> circles = new TreeMap<>();
	public Entity(final int id, final double x, final double y, final double vx, final double vy) {
		this.id = id;
		position = new ContinuousPoint(x, y);
		speed = new ContinuousPoint(vx, vy);
		direction = Geometry.from(position).vectorOf(vx, vy);
	}
	
	public Circle getCircle() {
		return getCircle(getRadius());
	}

	public abstract double getRadius();

	public abstract <Type> Type accept(EntityVisitor<Type> visitor);

	public boolean isBetween(final Entity entity, final Segment goal) {
		return isBetween(position, entity.position, goal.first);
	}
	
	public static boolean isBetween(final ContinuousPoint tested, final ContinuousPoint first, final ContinuousPoint second) {
		return (int) Math.signum(tested.getX()-first.getX())!=(int) Math.signum(tested.getX()-second.getX());
	}

	public Circle getCircle(final Double radius) {
		if(!circles.containsKey(radius)) {
			circles.put(radius, Geometry.from(position).cirleOf(radius));
		}
		return circles.get(radius);
	}

	public ContinuousPoint getNextPosition() {
		return direction.second;
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
		final Entity other = (Entity) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
}
