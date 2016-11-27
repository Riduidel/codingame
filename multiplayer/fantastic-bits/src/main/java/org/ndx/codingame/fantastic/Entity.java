package org.ndx.codingame.fantastic;

import java.util.Map;
import java.util.TreeMap;

import org.ndx.codingame.lib2d.Circle;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.Vector;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public abstract class Entity {
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
}
