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

		public Line lineTo(double x, double y) {
			return lineTo(at(x, y));
		}
		public Line lineTo(Point second) {
			return new Line(first, second);
		}
		
		public Circle cirleOf(double radius) {
			return new Circle(first, radius);
		}
	}
	public static final Point at(double x, double y) {
		return new Point(x, y);
	}

	public static final double ZERO = 0.00001;

	public static GeometryBuilder from(double x, double y) {
		return from(at(x, y));
	}
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