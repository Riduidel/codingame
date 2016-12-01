package org.ndx.codingame.lib2d;

import java.util.ArrayList;
import java.util.Collection;

import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.base.Distance2;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Line implements PointBuilder<ContinuousPoint>, Distance2 {
	public static class Coeffs {
		public final double a;
		public final double b;
		public final double c;
		public final double lineNorm;
		public final double squareNorm;
		public Coeffs(final ContinuousPoint first, final ContinuousPoint second) {
			a = second.y-first.y;
			b = first.x-second.x; 
			c = (first.y-second.y)*first.x + (second.x-first.x)*first.y;
			squareNorm = first.distance2SquaredTo(second);
			lineNorm = first.distance2To(second);
		}
		public double lineEquationFor(final AbstractPoint point) {
			return a*point.getX()+b*point.getY()+c;
		}

		protected double computeXFromY(final double y) {
			if(isHorizontalLine()) {
				throw new UnsupportedOperationException("can't compute x from y on vertical or horizontal line");
			}
			return -(b*y+c)/a;
		}

		protected double computeYFromX(final double x) {
			if(isVerticalLine()) {
				throw new UnsupportedOperationException("can't compute y from x on vertical or horizontal line");
			}
			return -(a*x+c)/b;
		}
		public boolean isVerticalLine() {
			return Algebra.isZero(b);
		}
		public boolean isHorizontalLine() {
			return Algebra.isZero(a);
		}
		public boolean matches(final ContinuousPoint point) {
			return Algebra.isZero(a*point.x+b*point.y+c);
		}
	}
	public class SegmentBuilder {

		private ContinuousPoint start;

		public SegmentBuilder startingAt(final ContinuousPoint second) {
			start = second;
			return this;
		}

		/**
		 * Here, length is an absolute one, as opposed to the one defined in {@link Line#pointAtNTimes(double)}
		 * @param length
		 * @return a Segment using the two points
		 */
		public Segment ofLength(final double length) {
			// first, get distance between the two points of the line
			final double referenceDistance = first.distance2To(second);
			final double multiplier = length/referenceDistance;
			return new Segment(start, pointAtNTimesOf(start, multiplier, Line.this));
		}
		
	}
	public final ContinuousPoint first;
	public final ContinuousPoint second;
	public final Coeffs coeffs;
	public Line(final ContinuousPoint first, final ContinuousPoint second) {
		super();
		this.first = first;
		this.second = second;
		coeffs = new Coeffs(first, second);
	}

	@Override
	public double distance2To(final AbstractPoint point) {
		return distance2ToLine(point);
	}

	private double distance2ToLine(final AbstractPoint point) {
		return Math.abs(coeffs.lineEquationFor(point))/coeffs.lineNorm;
	}
	
	public boolean contains(final ContinuousPoint point) {
		return coeffs.matches(point);
	}

	public ContinuousPoint project(final ContinuousPoint point) {
		return project(point, PointBuilder.DEFAULT);
	}

	public <Type extends ContinuousPoint> Type project(final Type point, final PointBuilder<Type> builder) {
		if(distance2ToLine(point)<0.001) {
			return point;
		}
		final double projection = ((second.x-first.x)*(point.x-second.x)+(second.y-first.y)*(point.y-second.y))/coeffs.squareNorm;
		// coordinates
		final double mx = second.x+(second.x-first.x)*projection;
		final double my = second.y+(second.y-first.y)*projection;
		return builder.build(mx, my);
	}

	public ContinuousPoint symetricOf(final ContinuousPoint point) {
		return symetricOf(point, PointBuilder.DEFAULT);
	}
		
	public <Type extends ContinuousPoint> Type symetricOf(final Type point, final PointBuilder<Type> builder) {
		if(distance2To(point)<0.001) {
			return point;
		}
		final Line orthogonal = orthogonal(point);
		return orthogonal.pointAtNTimes(2, builder);
	}

	public Line orthogonal(final ContinuousPoint point) {
		final ContinuousPoint projected = project(point);
		return new Line(point, projected);
	}

	/**
	 * Get the point at n times the distance between first and second point
	 * @param i
	 * @return
	 */
	public ContinuousPoint pointAtNTimes(final double i) {
		return pointAtNTimesOf(first, i, this);
	}

	public <Type extends ContinuousPoint> Type  pointAtNTimes(final double i, final PointBuilder<Type> builder) {
		return pointAtNTimesOf(first, i, builder);
	}

	protected <Type extends ContinuousPoint> Type pointAtNTimesOf(final ContinuousPoint p, final double i, final PointBuilder<Type> builder) {
		double x = p.x;
		double y = p.y;
		if(Algebra.isZero(first.distance2To(second))) {
			return builder.build(x, y);
		}
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
		return builder.build(x, y);
	}

	public double angleWith(final Line aim) {
		return aim.angle()-angle();
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
		result = prime * result + (first == null ? 0 : first.hashCode());
		result = prime * result + (second == null ? 0 : second.hashCode());
		return result;
	}
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Line other = (Line) obj;
		if (first == null) {
			if (other.first != null) {
				return false;
			}
		} else if (!first.equals(other.first)) {
			return false;
		}
		if (second == null) {
			if (other.second != null) {
				return false;
			}
		} else if (!second.equals(other.second)) {
			return false;
		}
		return true;
	}

	@Override
	public ContinuousPoint build(final double x, final double y) {
		if(x==first.x && y==first.y) {
			return first;
		} else if(x==second.x && y==second.y) {
			return second;
		}
		return new ContinuousPoint(x, y);
	}

	public <Type extends ContinuousPoint> Type pointAtAngle(final ContinuousPoint center, final int angle, final double radius, final PointBuilder<Type> builder) {
		final double combinedAngle = Math.toRadians(angle()+angle);
		return builder.build(
				center.x+Math.cos(combinedAngle)*radius,
				center.y+Math.sin(combinedAngle)*radius
				);
	}
	
	public Collection<ContinuousPoint> intersectionWith(final Circle circle) {
		return circle.intersectionWith(this);
	}

	public boolean intersectsWith(final Circle circle) {
		return circle.intersectsWith(this);
	}

	
	public Collection<ContinuousPoint> intersectionWith(final Line line) {
		final Collection<ContinuousPoint> returned = new ArrayList<>();
		final double a = coeffs.a,
				b = coeffs.b,
				c = coeffs.c,
				d = line.coeffs.a,
				e = line.coeffs.b,
				f = line.coeffs.c
				;
		if(e*a!=b*d) {
			if(coeffs.isHorizontalLine()) {
				/*
				 * a*x+b*y+c = 0
				 * d*x+e*y+f = 0
				 * 
				 * a*x = -(b*y+c)
				 * d*x = -(e*y+f)
				 * 
				 * x = -(b*y+c)/a
				 * x = -(e*y+f)/d
				 * 
				 * (b*y+c)/a=(e*y+f)/d
				 * 
				 * d*(b*y+c)=a*(e*y+f)
				 * 
				 * d*b*y+d*c=a*e*y+a*f
				 * 
				 * (d*b-a*e)*y=a*f-d*c
				 * 
				 * y=(a*f-d*c)/(d*b-a*e)
				 */
				final double y = (a*f-d*c)/(d*b-a*e);
				if(!line.coeffs.isHorizontalLine()) {
					returned.add(new ContinuousPoint(line.coeffs.computeXFromY(y), y));
				}
			} else {
				/*
				 * a*x+b*y+c = 0
				 * d*x+e*y+f = 0
				 * 
				 * b*y = -(a*x+c)
				 * e*y = -(d*x+f)
				 * 
				 * y = -(a*x+c)/b
				 * y = -(d*x+f)/e
				 * 
				 * (a*x+c)/b = (d*x+c)/e
				 * e*(a*x+c) = b*(d*x+f)
				 * 
				 * e*a*x+e*c = b*d*x+b*c
				 * (e*a-b*d)*x = b*f-e*c
				 * x = (b*f-e*c)/(e*a-b*d)
				 */
				final double x = (b*f-e*c)/(e*a-b*d);
				if(line.coeffs.isVerticalLine()) {
					returned.add(new ContinuousPoint(x, coeffs.computeYFromX(x)));
				} else {
					returned.add(new ContinuousPoint(x, line.coeffs.computeYFromX(x)));
				}
			}
		}
		return returned;
	}
	
	public boolean intersectsWith(final Line line) {
		return !intersectionWith(line).isEmpty();
	}
}
