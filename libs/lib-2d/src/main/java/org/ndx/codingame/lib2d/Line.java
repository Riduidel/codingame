package org.ndx.codingame.lib2d;

public class Line {
	public static class Coeffs {
		public final double a;
		public final double b;
		public final double c;
		public final double lineNorm;
		public final double squareNorm;
		public Coeffs(Point first, Point second) {
			this.a = second.y-first.y;
			this.b = second.x-first.x; 
			this.c = (first.y-second.y)*first.x + (second.x-first.x)*first.y;
			this.squareNorm = first.distance2SquaredTo(second);
			this.lineNorm = first.distance2To(second);
		}
		public double lineEquationFor(Point point) {
			return a*point.x+b*point.y+c;
		}

		protected double computeXFromY(double y) {
			if(isHorizontalLine()||isVerticalLine())
				throw new UnsupportedOperationException("can't compute x from y on vertical or horizontal line");
			return (b*y+c)/a;
		}

		protected double computeYFromX(double x) {
			if(isHorizontalLine()||isVerticalLine())
				throw new UnsupportedOperationException("can't compute y from x on vertical or horizontal line");
			return (a*x+c)/b;
		}
		public boolean isVerticalLine() {
			return Math.abs(b)<Geometry.ZERO;
		}
		public boolean isHorizontalLine() {
			return Math.abs(a)<Geometry.ZERO;
		}
		public boolean matches(Point point) {
			if(isVerticalLine()) {
				return Math.abs(a*point.x+c)<Geometry.ZERO;
			} else if(isHorizontalLine()) {
				return Math.abs(b*point.y+c)<Geometry.ZERO;
			} else {
				return Math.abs(a*point.x+b*point.y+c)<Geometry.ZERO;
			}
		}
	}
	public class SegmentBuilder {

		private Point start;

		public SegmentBuilder startingAt(Point second) {
			start = second;
			return this;
		}

		/**
		 * Here, length is an absolute one, as opposed to the one defined in {@link Line#pointAtNTimes(double)}
		 * @param length
		 * @return a Segment using the two points
		 */
		public Segment ofLength(double length) {
			// first, get distance between the two points of the line
			double referenceDistance = first.distance2To(second);
			double multiplier = length/referenceDistance;
			return new Segment(start, pointAtNTimesOf(start, multiplier));
		}
		
	}
	public final Point first;
	public final Point second;
	public final Coeffs coeffs;
	public Line(Point first, Point second) {
		super();
		this.first = first;
		this.second = second;
		this.coeffs = new Coeffs(first, second);
	}

	public double distanceTo(Point point) {
		return Math.abs(coeffs.lineEquationFor(point))/coeffs.lineNorm;
	}

	public Point project(Point point) {
		if(distanceTo(point)<0.001)
			return point;
		double projection = ((second.x-first.x)*(point.x-second.x)+(second.y-first.y)*(point.y-second.y))/coeffs.squareNorm;
		// coordinates
		double mx = second.x+(second.x-first.x)*projection;
		double my = second.y+(second.y-first.y)*projection;
		return new Point(mx, my);
	}

	public Point symetricOf(Point point) {
		if(distanceTo(point)<0.001)
			return point;
		Point projected = project(point);
		Line orthogonal = new Line(point, projected);
		return orthogonal.pointAtNTimes(2);
	}

	/**
	 * Get the point at n times the distance between first and second point
	 * @param i
	 * @return
	 */
	public Point pointAtNTimes(double i) {
		return pointAtNTimesOf(first, i);
	}

	protected Point pointAtNTimesOf(Point p, double i) {
		double x = 0;
		double y = 0;
		if(first.distance2To(second)<Geometry.ZERO)
			return first;
		if(coeffs.isHorizontalLine()) {
			y = p.y;
			x = i*(second.x-first.x)+p.x;
		} else if(coeffs.isVerticalLine()) {
			x = p.x;
			y = i*(second.y-first.y)+p.y;
		} else {
			x = i*(second.x-first.x)+p.x;
			y = coeffs.computeYFromX(x);
		}
		return new Point(x, y);
	}

	public double angleWith(Line aim) {
		return Math.toDegrees(aim.angle()-angle());
	}

	public double angle() {
		return Math.toDegrees(Math.atan2(second.y-first.y, second.x-first.x));
	}
	

	public SegmentBuilder segment() {
		return new SegmentBuilder();
	}

	public Segment getDefiningSegment() {
		return new Segment(first, second);
	}	
	@Override
	public String toString() {
		return "Line [first=" + first + ", second=" + second + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
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
		Line other = (Line) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
}
