package org.ndx.codingame.lib2d.base;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public abstract class AbstractPoint {
	public static class PositionByDistanceTo implements Comparator<AbstractPoint> {
	
		private AbstractPoint center;
	
		public PositionByDistanceTo(ContinuousPoint principal) {
			this.center = principal;
		}
	
		@Override
		public int compare(AbstractPoint o1, AbstractPoint o2) {
			return (int) Math.signum(o1.distance2SquaredTo(center)-o2.distance2SquaredTo(center));
		}
	}
	private Map<Object, Double> distanceCache;
	public abstract double getX();
	public abstract double getY();
	public double distance1To(AbstractPoint other) {
		return Math.abs(getX()-other.getX())+Math.abs(getY()-other.getY());
	}
	public double computeDistance2To(AbstractPoint other) {
		return Math.sqrt(distance2SquaredTo(other));
	}

	public double distance2SquaredTo(AbstractPoint other) {
		return (getX()-other.getX())*(getX()-other.getX())+(getY()-other.getY())*(getY()-other.getY());
	}
	public double distance2To(AbstractPoint other) {
		if(!getDistanceCache().containsKey(other))
			getDistanceCache().put(other, computeDistance2To(other));
		return getDistanceCache().get(other);
	}
	public double minDistance2To(Collection<? extends AbstractPoint> dangerous) {
		if(!getDistanceCache().containsKey(dangerous)) {
			getDistanceCache().put(dangerous, computeMinDistance2To(dangerous));
		}
		return getDistanceCache().get(dangerous);
	}
	private double computeMinDistance2To(Collection<? extends AbstractPoint> dangerous) {
		double minDistance = Integer.MAX_VALUE;
		for (AbstractPoint p : dangerous) {
			double distance = distance2To(p);
			if(distance<minDistance)
				minDistance = distance;
		}
		return minDistance;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(getX());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(getY());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!AbstractPoint.class.isInstance(obj))
			return false;
		AbstractPoint other = (AbstractPoint) obj;
		if (Double.doubleToLongBits(getX()) != Double.doubleToLongBits(other.getX()))
			return false;
		if (Double.doubleToLongBits(getY()) != Double.doubleToLongBits(other.getY()))
			return false;
		return true;
	}

	private Map<Object, Double> getDistanceCache() {
		if(distanceCache==null)
			distanceCache = new WeakHashMap<>();
		return distanceCache;
	}
	public <Type extends AbstractPoint> Type findNearestDistance2(Collection<? extends Type> keySet) {
		Type returned = null;
		for(Type element : keySet) {
			if(returned==null) {
				returned = element;
			} else {
				if(distance2SquaredTo(element)<distance2SquaredTo(returned)) {
					returned = element;
				}
			}
		}
		return returned;
	}
}
