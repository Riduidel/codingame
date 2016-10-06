package org.ndx.codingame.lib2d;

import java.util.Collection;

/**
 * Helper class containing builder patterns
 * @author ndelsaux
 *
 */
public class Geometry {
	public static class GeometryBuilder {

		private Point first;

		public GeometryBuilder(Point at) {
			this.first = at;
		}

		public Line lineTo(Point second) {
			return new Line(first, second);
		}
		
		public Rectangle rectangleTo(Point second) {
			return new Rectangle(
					Math.max(first.y, second.y),
					Math.min(first.y, first.y),
					Math.min(first.x, second.y),
					Math.max(first.x, second.y)
					);
		}
	}
	public static final Point at(double x, double y) {
		return new Point(x, y);
	}

	public static final double ZERO = 0.00001;

	public static GeometryBuilder from(Point at) {
		return new GeometryBuilder(at);
	}

	public static Point barycenterOf(Collection<? extends Point> points) {
		double x = 0,
				y = 0;
		int size = points.size();
		for (Point point : points) {
			x+=point.x;
			y+=point.y;
		}
		return new Point(x/size, y/size);
	}
}