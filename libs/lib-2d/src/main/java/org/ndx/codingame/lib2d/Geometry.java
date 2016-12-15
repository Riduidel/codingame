package org.ndx.codingame.lib2d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.continuous.bezier.BezierCurve;
import org.ndx.codingame.lib2d.continuous.bezier.PolynomialBezierCurve;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.shapes.Circle;
import org.ndx.codingame.lib2d.shapes.Line;
import org.ndx.codingame.lib2d.shapes.Polygon;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.lib2d.shapes.Triangle;
import org.ndx.codingame.lib2d.shapes.Vector;

/**
 * Helper class containing builder patterns
 * @author ndelsaux
 *
 */
public class Geometry {
	public static class GeometryBuilder {
		public static class BezierCurveBuilder {
			private final List<ContinuousPoint> control = new ArrayList<>();
			private final ContinuousPoint from;
			private final ContinuousPoint to;

			public BezierCurveBuilder(final ContinuousPoint first, final ContinuousPoint end) {
				from = first;
				to = end;
			}
			
			public BezierCurveBuilder control(final ContinuousPoint cp) {
				control.add(cp);
				return this;
			}
			
			public BezierCurve build() {
				return new PolynomialBezierCurve(from, control, to);
			}
		}
		public static class PolygonBuilder {
			private final List<ContinuousPoint> points = new ArrayList<>();
			public PolygonBuilder through(final ContinuousPoint p) {
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

		private final ContinuousPoint first;

		public GeometryBuilder(final ContinuousPoint at) {
			first = at;
		}

		public Line lineTo(final double x, final double y) {
			return lineTo(at(x, y));
		}
		public Line lineTo(final ContinuousPoint second) {
			return new Line(first, second);
		}
		
		public Line lineTo(final DiscretePoint second) {
			return new Line(first, continuous(second));
		}
		
		public Circle cirleOf(final double radius) {
			return new Circle(first, radius);
		}

		public Segment segmentTo(final double x, final double y) {
			return segmentTo(at(x, y));
		}

		public Segment segmentTo(final DiscretePoint p) {
			return segmentTo(continuous(p));
		}
		public Segment segmentTo(final ContinuousPoint p) {
			return new Segment(first, p);
		}
		
		public PolygonBuilder through(final ContinuousPoint p) {
			return new PolygonBuilder().through(first).through(p);
		}
		
		public BezierCurveBuilder to(final ContinuousPoint end) {
			return new BezierCurveBuilder(first, end);
		}

		/**
		 * Beware, second point must be an absolute position
		 * @param point
		 * @return
		 */
		public Vector vectorOf(final ContinuousPoint point) {
			return new Vector(first, point);
		}
		public Vector vectorOf(final double vx, final double vy) {
			return vectorOf(new ContinuousPoint(first.x+vx, first.y+vy));
		}
	}
	public static final ContinuousPoint at(final double x, final double y) {
		return new ContinuousPoint(x, y);
	}

	public static final DiscretePoint at(final int x, final int y) {
		return new DiscretePoint(x, y);
	}

	public static GeometryBuilder from(final int x, final int y) {
		return from(at(x, y));
	}
	public static GeometryBuilder from(final double x, final double y) {
		return from(at(x, y));
	}
	public static GeometryBuilder from(final ContinuousPoint at) {
		return new GeometryBuilder(at);
	}
	
	public static GeometryBuilder from(final DiscretePoint at) {
		return new GeometryBuilder(continuous(at));
	}
	
	public static ContinuousPoint continuous(final DiscretePoint point) {
		return new ContinuousPoint(point.x, point.y);
	}

	public static ContinuousPoint barycenterOf(final Collection<? extends ContinuousPoint> points) {
		double x = 0,
				y = 0;
		final int size = points.size();
		for (final ContinuousPoint point : points) {
			x+=point.x;
			y+=point.y;
		}
		return new ContinuousPoint(x/size, y/size);
	}
}