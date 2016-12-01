package org.ndx.codingame.lib2d.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class AbstractPoint implements Distance2 {
	public static class PositionByDistance2To implements Comparator<AbstractPoint> {
	
		private final Collection<Distance2> centers = new ArrayList<>();
		private int signum = 1;
	
		public PositionByDistance2To(final Distance2... principal) {
			centers.addAll(Arrays.asList(principal));
		}
		
		public PositionByDistance2To(final Collection<Distance2> principal) {
			centers.addAll(principal);
		}
		
		@Override
		public PositionByDistance2To reversed() {
			final PositionByDistance2To reversed = new PositionByDistance2To(centers);
			reversed.signum = -1;
			return reversed;
		}

		private double distance2To(final AbstractPoint point) {
			return centers.stream()
					.mapToDouble(c -> c.distance2To(point))
					.sum();
		}
	
		@Override
		public int compare(final AbstractPoint o1, final AbstractPoint o2) {
			return signum * (int) Math.signum(distance2To(o1)-distance2To(o2));
		}
	}
	private Map<Object, Double> distanceCache;
	public abstract double getX();
	public abstract double getY();
	public double distance1To(final AbstractPoint other) {
		return Math.abs(getX()-other.getX())+Math.abs(getY()-other.getY());
	}
	public double computeDistance2To(final AbstractPoint other) {
		return Math.sqrt(distance2SquaredTo(other));
	}

	public double distance2SquaredTo(final AbstractPoint other) {
		return (getX()-other.getX())*(getX()-other.getX())+(getY()-other.getY())*(getY()-other.getY());
	}
	@Override
	public double distance2To(final AbstractPoint other) {
		if(!getDistanceCache().containsKey(other)) {
			getDistanceCache().put(other, computeDistance2To(other));
		}
		return getDistanceCache().get(other);
	}
	public double minDistance2To(final Collection<? extends AbstractPoint> dangerous) {
		if(!getDistanceCache().containsKey(dangerous)) {
			getDistanceCache().put(dangerous, computeMinDistance2To(dangerous));
		}
		return getDistanceCache().get(dangerous);
	}
	private double computeMinDistance2To(final Collection<? extends AbstractPoint> dangerous) {
		double minDistance = Integer.MAX_VALUE;
		for (final AbstractPoint p : dangerous) {
			final double distance = distance2To(p);
			if(distance<minDistance) {
				minDistance = distance;
			}
		}
		return minDistance;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(getX());
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(getY());
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
		if (!AbstractPoint.class.isInstance(obj)) {
			return false;
		}
		final AbstractPoint other = (AbstractPoint) obj;
		if (Double.doubleToLongBits(getX()) != Double.doubleToLongBits(other.getX())) {
			return false;
		}
		if (Double.doubleToLongBits(getY()) != Double.doubleToLongBits(other.getY())) {
			return false;
		}
		return true;
	}

	private Map<Object, Double> getDistanceCache() {
		if(distanceCache==null) {
			distanceCache = new WeakHashMap<>();
		}
		return distanceCache;
	}
	public <Type extends AbstractPoint> Type findNearestDistance2(final Collection<? extends Type> keySet) {
		Type returned = null;
		for(final Type element : keySet) {
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
