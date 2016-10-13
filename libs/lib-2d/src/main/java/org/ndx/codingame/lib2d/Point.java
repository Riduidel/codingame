package org.ndx.codingame.lib2d;

public class Point {
	public final double x;
	public final double y;
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public double distance1To(Point other) {
		return Math.abs(x-other.x)+Math.abs(y-other.y);
	}
	public double distance2SquaredTo(Point other) {
		return (x-other.x)*(x-other.x)+(y-other.y)*(y-other.y);
	}
	public double distance2To(Point other) {
		return Math.sqrt(distance2SquaredTo(other));
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
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
		Point other = (Point) obj;
		if (!Algebra.isEquals(x, other.x))
			return false;
		if (!Algebra.isEquals(y, other.y))
			return false;
		return true;
	}
}
