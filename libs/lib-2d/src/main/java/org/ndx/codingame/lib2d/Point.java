package org.ndx.codingame.lib2d;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public class Point {
	private final Map<Object, Double> distanceCache = new WeakHashMap<>();
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
		if(!distanceCache.containsKey(other))
			distanceCache.put(other, computeDistance2To(other));
		return distanceCache.get(other);
	}
	private double computeDistance2To(Point other) {
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
	public double minDistance2To(Collection<? extends Point> dangerous) {
		if(!distanceCache.containsKey(dangerous)) {
			distanceCache.put(dangerous, computeMinDistanceTo(dangerous));
		}
		return distanceCache.get(dangerous);
	}
	private double computeMinDistanceTo(Collection<? extends Point> dangerous) {
		double minDistance = Integer.MAX_VALUE;
		for (Point p : dangerous) {
			double distance = distance2To(p);
			if(distance<minDistance)
				minDistance = distance;
		}
		return minDistance;
	}
}
