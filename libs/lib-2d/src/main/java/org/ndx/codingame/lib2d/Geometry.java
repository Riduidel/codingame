package org.ndx.codingame.lib2d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helper class containing builder patterns
 * @author ndelsaux
 *
 */
public class Geometry {
	public static class GeometryBuilder {
		public static class PolygonBuilder {
			private List<Point> points = new ArrayList<Point>();
			public PolygonBuilder through(Point p) {
				points.add(p);
				return this;
			}
			public Polygon build() {
				switch(points.size()) {
				case 3:
					return Triangle.from(points.get(0), points.get(1), points.get(2));
//				case 4:
//					return Quadrilater.from(points.get(0), points.get(1), points.get(2), points.get(3));
				default:
					throw new UnsupportedOperationException("there is no polygon with %d points");
				}
			}
		}

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

		public Segment segmentTo(double x, double y) {
			return segmentTo(at(x, y));
		}

		public Segment segmentTo(Point p) {
			return new Segment(first, p);
		}
		
		public PolygonBuilder through(Point p) {
			return new PolygonBuilder().through(first).through(p);
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