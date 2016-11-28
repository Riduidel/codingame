package org.ndx.codingame.lib2d;

import java.util.Comparator;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Vector extends Segment {

	public static class PositionByDistanceTo implements Comparator<ContinuousPoint> {

		private Vector vector;

		public PositionByDistanceTo(Vector vector) {
			this.vector = vector;
		}

		@Override
		public int compare(ContinuousPoint o1, ContinuousPoint o2) {
			ContinuousPoint projected1 = vector.project(o1);
			ContinuousPoint projected2 = vector.project(o2);
			int returned = (int) Math.signum(vector.first.distance2SquaredTo(projected1)-vector.first.distance2SquaredTo(projected2));
			if(returned==0) {
				returned = (int) Math.signum(o1.distance2SquaredTo(projected1)-o2.distance2SquaredTo(projected2));
			}
			return returned;
		}

	}

	public Vector(ContinuousPoint first, ContinuousPoint second) {
		super(first, second);
	}

}
