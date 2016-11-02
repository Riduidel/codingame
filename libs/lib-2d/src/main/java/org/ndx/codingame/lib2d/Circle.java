package org.ndx.codingame.lib2d;

import static java.lang.Math.pow;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Circle {

	public final ContinuousPoint center;
	public final double radius;
	private final double squaredRadius;

	public Circle(ContinuousPoint center, double radius) {
		this.center = center;
		this.radius = radius;
		this.squaredRadius = pow(radius, 2);
	}
	public boolean includesOrContains(AbstractPoint point) {
		return center.distance2SquaredTo(point)<=squaredRadius;
	}
	
	public boolean includes(AbstractPoint point) {
		return center.distance2SquaredTo(point)<squaredRadius;
	}

	public boolean contains(AbstractPoint point) {
		return center.distance2SquaredTo(point)==squaredRadius;
	}

	public Collection<ContinuousPoint> intersectionWith(Circle other) {
		if(equals(other)) {
			return Collections.emptyList();
		} else if(center.distance2To(other.center)>radius+other.radius) {
			return Collections.emptyList();
		} else if(center.distance2To(other.center)==radius+other.radius) {
			Segment segment = new Segment(center, other.center);
			return Arrays.asList(segment.pointAtDistance(center, radius, PointBuilder.DEFAULT));
		} else {
			double x0 = center.x,
					x0_2 = pow(x0, 2),
					y0 = center.y,
					y0_2 = pow(y0, 2),
					r0 = radius,
					r0_2 = squaredRadius,
					x1 = other.center.x,
					x1_2 = pow(x1, 2),
					y1 = other.center.y,
					y1_2 = pow(y1, 2),
					r1 = other.radius,
					r1_2 = other.squaredRadius,
					A,
					B,
					C,
					result_x_0,
					result_y_0,
					result_x_1,
					result_y_1;
			// Now come the hard part : solve the point equation
			// using as reference http://math.15873.pagespro-orange.fr/IntCercl.html
			if(Algebra.isEquals(y0, y1)) {
				result_x_0 = result_x_1 = 
						(r1_2-r0_2-x1_2+x0_2)/(2*(x0-x1));

				// degragded equation for aligned circles
				A = 1;
				B = -2*y1;
				C = x1_2+result_x_0*result_x_0-2*x1*result_x_0+y1_2-r1_2;

				double[] solutions= Algebra.solutionsOf(A, B, C);
				result_y_0 = solutions[0];
				result_y_1 = solutions[1];
			} else {
				double coeff = (x0-x1)/(y0-y1);
				
				double N = (r1_2-r0_2-x1_2+x0_2-y1_2+y0_2)/(2*(y0-y1));
				
				A = coeff*coeff+1;
				B = 2*y0*coeff-2*N*coeff-2*x0;
				C = x0_2+y0_2+N*N-r0_2-2*y0*N;
				
				double[] solutions= Algebra.solutionsOf(A, B, C);
				
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
	
	public Collection<ContinuousPoint> intersectionWith(Segment segment) {
		return segment.intersectionWith(this);
	}
	
	public Collection<ContinuousPoint> intersectionWith(Line line) {
		if(Algebra.isZero(line.coeffs.b)) {
			double a = -line.coeffs.b/line.coeffs.a,
					b = -line.coeffs.c/line.coeffs.a;
			double A = 1+pow(a, 2);
			double B = 2*(a*(b-center.y)-center.y);
			double C = pow(center.y, 2) + pow(b-center.x, 2) - squaredRadius;
			double[] solutions = Algebra.solutionsOf(A, B, C);
			Collection<ContinuousPoint> returned = new HashSet<>();
			for(double y : solutions) {
				returned.add(new ContinuousPoint(line.coeffs.computeXFromY(y), y));
			}
			return returned;
		} else {
			double a = -line.coeffs.a/line.coeffs.b,
					b = -line.coeffs.c/line.coeffs.b;
			double A = 1+pow(a, 2);
			double B = 2*(a*(b-center.y)-center.x);
			double C = pow(center.x, 2) + pow(b-center.y, 2) - squaredRadius;
			double[] solutions = Algebra.solutionsOf(A, B, C);
			Collection<ContinuousPoint> returned = new HashSet<>();
			for(double x : solutions) {
				returned.add(new ContinuousPoint(x, line.coeffs.computeYFromX(x)));
			}
			return returned;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		long temp;
		temp = Double.doubleToLongBits(radius);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Circle other = (Circle) obj;
		if (center == null) {
			if (other.center != null)
				return false;
		} else if (!center.equals(other.center))
			return false;
		if (Algebra.isEquals(radius, other.radius))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Circle [center=" + center + ", radius=" + radius + "]";
	}
}
