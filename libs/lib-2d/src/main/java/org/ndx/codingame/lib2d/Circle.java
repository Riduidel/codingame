package org.ndx.codingame.lib2d;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class Circle {

	public final Point center;
	public final double radius;
	private final double squaredRadius;

	public Circle(Point center, double radius) {
		this.center = center;
		this.radius = radius;
		this.squaredRadius = radius*radius;
	}
	public boolean includesOrContains(Point point) {
		return center.distance2SquaredTo(point)<=squaredRadius;
	}
	
	public boolean includes(Point point) {
		return center.distance2SquaredTo(point)<squaredRadius;
	}

	public boolean contains(Point point) {
		return center.distance2SquaredTo(point)==squaredRadius;
	}

	public Collection<Point> intersectionWith(Circle other) {
		if(equals(other)) {
			return Collections.emptyList();
		} else if(center.distance2To(other.center)>radius+other.radius) {
			return Collections.emptyList();
		} else if(center.distance2To(other.center)==radius+other.radius) {
			Segment segment = new Segment(center, other.center);
			return Arrays.asList(segment.pointAtDistance(center, radius, PointBuilder.DEFAULT));
		} else {
			double x0 = center.x,
					x0_2 = x0*x0,
					y0 = center.y,
					y0_2 = y0*y0,
					r0 = radius,
					r0_2 = squaredRadius,
					x1 = other.center.x,
					x1_2 = x1*x1,
					y1 = other.center.y,
					y1_2 = y1*y1,
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
			if(Math.abs(y0-y1)<Geometry.ZERO) {
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
					new Point(result_x_0, result_y_0),
					new Point(result_x_1, result_y_1));
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
		if (Double.doubleToLongBits(radius) != Double.doubleToLongBits(other.radius))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Circle [center=" + center + ", radius=" + radius + "]";
	}
}
