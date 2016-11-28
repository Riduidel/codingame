package org.ndx.codingame.fantastic.entities;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.ndx.codingame.lib2d.Circle;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.Vector;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public abstract class Entity {
	public static class ByDistanceTo implements Comparator<Entity> {

		private AbstractPoint.PositionByDistanceTo center;

		public ByDistanceTo(Entity principal) {
			this.center = new AbstractPoint.PositionByDistanceTo(principal.position);
		}

		@Override
		public int compare(Entity o1, Entity o2) {
			return center.compare(o1.position, o2.position);
		}
	}

	public final ContinuousPoint position;
	public final Vector direction;
	public final int id;
	private Map<Double, Circle> circles = new TreeMap<>();
	public Entity(int id, double x, double y, double vx, double vy) {
		this.id = id;
		position = new ContinuousPoint(x, y);
		direction = Geometry.from(position).vectorOf(vx, vy);
	}
	
	public Circle getCircle() {
		return getExtendedCircle(getRadius());
	}

	protected abstract double getRadius();

	public abstract <Type> Type accept(EntityVisitor<Type> visitor);

	public boolean isBetween(Wizard wizard, Segment goal) {
		return (int) Math.signum(position.getX()-wizard.position.getX())!=(int) Math.signum(position.getX()-goal.first.getX());
	}

	public Circle getExtendedCircle(Double radius) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
