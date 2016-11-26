package org.ndx.codingame.fantastic;

import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.Vector;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public abstract class Entity {
	public final ContinuousPoint position;
	public final Vector direction;
	public final int id;
	public Entity(int id, double x, double y, double vx, double vy) {
		this.id = id;
		position = new ContinuousPoint(x, y);
		direction = Geometry.from(position).vectorOf(vx, vy);
	}

	public abstract <Type> Type accept(EntityVisitor<Type> visitor);
}
