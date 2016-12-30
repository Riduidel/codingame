package org.ndx.codingame.lib2d.shapes;

import java.util.Comparator;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Vector extends Segment {

	public static class PositionByDistanceTo implements Comparator<ContinuousPoint> {

		private final Vector vector;

		public PositionByDistanceTo(final Vector vector) {
			this.vector = vector;
		}

		@Override
		public int compare(final ContinuousPoint o1, final ContinuousPoint o2) {
			final ContinuousPoint projected1 = vector.project(o1);
			final ContinuousPoint projected2 = vector.project(o2);
			int returned = (int) Math.signum(vector.first.distance2SquaredTo(projected1)-vector.first.distance2SquaredTo(projected2));
			if(returned==0) {
				returned = (int) Math.signum(o1.distance2SquaredTo(projected1)-o2.distance2SquaredTo(projected2));
			}
			return returned;
		}

	}

	public Vector(final ContinuousPoint first, final ContinuousPoint second) {
		super(first, second);
	}

	public double getX() {
		return second.getX()-first.getX();
	}

	public double getY() {
		return second.getY()-first.getY();
	}

	public Segment toSegment() {
		return new Segment(first, second);
	}

	@Override
	public Line toLine() {
		return new Line(first, second);
	}

	@Override
	protected Line create(final ContinuousPoint targetPosition, final ContinuousPoint secondTarget) {
		return new Vector(targetPosition, secondTarget);
	}
}
