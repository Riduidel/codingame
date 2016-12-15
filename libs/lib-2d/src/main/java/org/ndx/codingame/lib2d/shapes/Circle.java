package org.ndx.codingame.lib2d.shapes;

import static java.lang.Math.pow;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.ndx.codingame.lib2d.Algebra;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Circle {

	public final ContinuousPoint center;
	public final double radius;
	private final double squaredRadius;

	public Circle(final ContinuousPoint center, final double radius) {
		this.center = center;
		this.radius = radius;
		squaredRadius = pow(radius, 2);
	}
	public boolean includesOrContains(final AbstractPoint point) {
		return center.distance2SquaredTo(point)<=squaredRadius;
	}
	
	public boolean includes(final AbstractPoint point) {
		return center.distance2SquaredTo(point)<squaredRadius;
	}

	public boolean contains(final AbstractPoint point) {
		return center.distance2SquaredTo(point)==squaredRadius;
	}

	public Collection<ContinuousPoint> intersectionWith(final Circle other) {
		if(equals(other)) {
			return Collections.emptyList();
		} else if(center.distance2To(other.center)>radius+other.radius) {
			return Collections.emptyList();
		} else if(center.distance2To(other.center)==radius+other.radius) {
			final Segment segment = new Segment(center, other.center);
			return Arrays.asList(segment.pointAtDistance(center, radius, center));
		} else {
			final double x0 = center.x,
					x0_2 = pow(x0, 2),
					y0 = center.y,
					y0_2 = pow(y0, 2),
					r0_2 = squaredRadius,
					x1 = other.center.x,
					x1_2 = pow(x1, 2),
					y1 = other.center.y,
					y1_2 = pow(y1, 2),
					r1_2 = other.squaredRadius;
			double A, B, C, result_x_0, result_y_0, result_x_1, result_y_1;
			// Now come the hard part : solve the point equation
			// using as reference http://math.15873.pagespro-orange.fr/IntCercl.html
			if(Algebra.isEquals(y0, y1)) {
				result_x_0 = result_x_1 = 
						(r1_2-r0_2-x1_2+x0_2)/(2*(x0-x1));

				// degragded equation for aligned circles
				A = 1;
				B = -2*y1;
				C = x1_2+result_x_0*result_x_0-2*x1*result_x_0+y1_2-r1_2;

				final double[] solutions= Algebra.solutionsOf(A, B, C);
				result_y_0 = solutions[0];
				result_y_1 = solutions[1];
			} else {
				final double coeff = (x0-x1)/(y0-y1);
				
				final double N = (r1_2-r0_2-x1_2+x0_2-y1_2+y0_2)/(2*(y0-y1));
				
				A = coeff*coeff+1;
				B = 2*y0*coeff-2*N*coeff-2*x0;
				C = x0_2+y0_2+N*N-r0_2-2*y0*N;
				
				final double[] solutions= Algebra.solutionsOf(A, B, C);
				
				result_x_0 = solutions[0];
				result_x_1 = solutions[1];
				
				result_y_0 = N-result_x_0*coeff;
				result_y_1 = N-result_x_1*coeff;
			}
			
			return Arrays.asList(
					new ContinuousPoint(result_x_0, result_y_0),
					new ContinuousPoint(result_x_1, result_y_1));
		}
	}
	
	public Collection<ContinuousPoint> intersectionWith(final Segment segment) {
		return segment.intersectionWith(this);
	}
	public boolean intersectsWith(final Line line) {
		return !intersectionWith(line).isEmpty();
	}
	
	public Collection<ContinuousPoint> intersectionWith(final Line line) {
		if(Algebra.isZero(line.coeffs.b)) {
			final double a = -line.coeffs.b/line.coeffs.a,
					b = -line.coeffs.c/line.coeffs.a;
			final double A = 1+pow(a, 2);
			final double B = 2*(a*(b-center.y)-center.y);
			final double C = pow(center.y, 2) + pow(b-center.x, 2) - squaredRadius;
			final double[] solutions = Algebra.solutionsOf(A, B, C);
			final Collection<ContinuousPoint> returned = new HashSet<>();
			for(final double y : solutions) {
				returned.add(new ContinuousPoint(line.coeffs.computeXFromY(y), y));
			}
			return returned;
		} else {
			final double a = -line.coeffs.a/line.coeffs.b,
					b = -line.coeffs.c/line.coeffs.b;
			final double A = 1+pow(a, 2);
			final double B = 2*(a*(b-center.y)-center.x);
			final double C = pow(center.x, 2) + pow(b-center.y, 2) - squaredRadius;
			final double[] solutions = Algebra.solutionsOf(A, B, C);
			final Collection<ContinuousPoint> returned = new HashSet<>();
			for(final double x : solutions) {
				returned.add(new ContinuousPoint(x, line.coeffs.computeYFromX(x)));
			}
			return returned;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (center == null ? 0 : center.hashCode());
		long temp;
		temp = Double.doubleToLongBits(radius);
		result = prime * result + (int) (temp ^ temp >>> 32);
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
		final Circle other = (Circle) obj;
		if (center == null) {
			if (other.center != null) {
				return false;
			}
		} else if (!center.equals(other.center)) {
			return false;
		}
		if (Algebra.isEquals(radius, other.radius)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Circle [center=" + center + ", radius=" + radius + "]";
	}
}
