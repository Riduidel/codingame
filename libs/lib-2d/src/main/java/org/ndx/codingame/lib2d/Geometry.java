package org.ndx.codingame.lib2d;

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
		
	}
	public static final Point at(double x, double y) {
		return new Point(x, y);
	}

	public static final double ZERO = 0.00001;

	public static GeometryBuilder from(Point at) {
		return new GeometryBuilder(at);
	}
}