package org.ndx.codingame.spring.challenge.playground;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.spring.challenge.entities.Content;

public class NearestPointCache {
	public static final class ByDistanceOnPlayground extends AbstractPoint.PositionByDistance2To {

		private ImmutablePlayground<Double> distanceOnPlayground;

		public ByDistanceOnPlayground(DiscretePoint point, ImmutablePlayground<Double> immutablePlayground) {
			super(point);
			super.distinct = true;
			this.distanceOnPlayground = immutablePlayground;
		}
		
		@Override
		public ByDistanceOnPlayground reversed() {
			final ByDistanceOnPlayground reversed = new ByDistanceOnPlayground((DiscretePoint) centers.iterator().next(), distanceOnPlayground);
			reversed.signum = -1*signum;
			return reversed;
		}
		
		@Override
		public int compare(AbstractPoint o1, AbstractPoint o2) {
			// I know they're discrete point, ok ?
			DiscretePoint p1 = (DiscretePoint) o1, p2 = (DiscretePoint) o2;
			
			Double d1 = distanceOnPlayground.get(p1),
					d2 = distanceOnPlayground.get(p2);
			int doubleCompare = d1.compareTo(d2);
			if(doubleCompare!=0) {
				return doubleCompare;
			}
			return super.compare(o1, o2);
		}
	}

	/**
	 * Sort all points of playground based upon distance to the point this set is sorted in.
	 */
	private Playground<SortedSet<DiscretePoint>> nearestPoints;
	private List<DiscretePoint> locations;
	private Cache cache;
	private Iterator<DiscretePoint> locationsIterator;
	private long worstDuration;
	private boolean cacheLoaded;
	public NearestPointCache(Cache cache, ImmutablePlayground<Content> playfield, List<DiscretePoint> locations) {
		this.cache = cache;
		this.locations = locations;
		nearestPoints = new Playground<>(playfield.getWidth(), playfield.getHeight());
		locationsIterator = locations.iterator();
	}

	public SortedSet<DiscretePoint> getPointsSortedByDistanceTo(DiscretePoint point) {
		if(nearestPoints.get(point)==null) {
			SortedSet<DiscretePoint> sorted = new TreeSet<>(new ByDistanceOnPlayground(point, cache.usingDistanceTo(point)));
			sorted.addAll(locations);
			nearestPoints.set(point, sorted);
		}
		return nearestPoints.get(point);
	}

	public void loadPointsByDistanceUntil(Delay delay, int limit) {
		if(!cacheLoaded) {
			while(locationsIterator.hasNext() && delay.howLong()<limit-worstDuration) {
				Delay loadDuration = new Delay();
				DiscretePoint toCompute = locationsIterator.next();
				getPointsSortedByDistanceTo(toCompute);
				worstDuration = Math.max(loadDuration.howLong(), worstDuration);
			}
			cacheLoaded = !locationsIterator.hasNext();
			System.err.println("Points by distance have been loaded! worst duration is "+worstDuration);
		}
	}

	public boolean loaded() {
		return cacheLoaded;
	}

}
