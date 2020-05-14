package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.Pac;

public class Cache {
	public static final class ByDistanceOnPlayground extends AbstractPoint.PositionByDistance2To {

		private ImmutablePlayground<Double> distanceOnPlayground;

		public ByDistanceOnPlayground(DiscretePoint point, ImmutablePlayground<Double> immutablePlayground) {
			super(point);
			this.distanceOnPlayground = immutablePlayground;
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
	public static final int NEXT_POINTS_VIEW_RANGE = Integer.MAX_VALUE;
	public static final int NEXT_POINTS_SPEED = 2;
	public static final int NEXT_POINTS_NORMAL = 1;
	public static final List<Integer> NEXT_POINTS_LIST = Arrays.asList(
			NEXT_POINTS_NORMAL,
			NEXT_POINTS_SPEED,
			NEXT_POINTS_VIEW_RANGE);

	final Map<Integer, ImmutablePlayground<List<List<DiscretePoint>>>> nextPointsCache = new HashMap<>();
	final ImmutablePlayground<ScoringSystem> distancesToPoints;
	final ImmutablePlayground<List<Direction>> directions;
	private final Playground<SortedSet<DiscretePoint>> nearestPoints;
	private List<DiscretePoint> locations;

	public Cache(Playfield playfield) {
		// Ccompute list of valid locations (in order to avoid browsing the whole table while walls don't interest us)
		locations = computeValidLocations(playfield);
		// Compute distance2 for each point
		distancesToPoints = computeDistancesToPoints(playfield, locations);
		// Compute valid directions for each point
		directions = computePossibleDirections(playfield, locations);
		for(Integer i : NEXT_POINTS_LIST) {
			nextPointsCache.put(i, computeNextPointsList(playfield, directions, locations, i));
		}
		nearestPoints = new Playground<>(playfield.getWidth(), playfield.getHeight());
	}

	private List<DiscretePoint> computeValidLocations(Playfield playfield) {
		return playfield.accept(new PlaygroundAdapter<List<DiscretePoint>, Content>() {
			@Override
			public void startVisit(ImmutablePlayground<Content> playground) {
				returned = new ArrayList<>();
			}
			
			@Override
			public void visit(int x, int y, Content content) {
				if(playfield.allow(x, y)) {
					returned.add(new DiscretePoint(x, y));
				}
			}
		});
	}

	private ImmutablePlayground<ScoringSystem> computeDistancesToPoints(Playfield playfield, List<DiscretePoint> locations) {
		Playground<ScoringSystem> returned = new Playground<>(playfield.getWidth(), playfield.getHeight());
		for(DiscretePoint point : locations) {
			returned.set(point, new ScoringSystem(playfield.get(point), 
					computeDistancesTo(playfield, point, NEXT_POINTS_VIEW_RANGE)));
		}
		return returned;
	}

	private ImmutablePlayground<List<List<DiscretePoint>>> computeNextPointsList(Playfield playfield, ImmutablePlayground<List<Direction>> directions,
			List<DiscretePoint> locations, int limit) {
		Playground<List<List<DiscretePoint>>> points = new Playground<>(playfield.getWidth(), playfield.getHeight());
		for(DiscretePoint point : locations) {
			List<List<DiscretePoint>> successors = computeNextPointsListAt(playfield, directions, point, limit);
			points.set(point, successors);
		}
		return points;
	}

	List<List<DiscretePoint>> computeNextPointsListAt(
			Playfield playfield,
			ImmutablePlayground<List<Direction>> directions,
			DiscretePoint p, int limit) {
		List<Direction> directionsFor = directions.get(p);
		List<List<DiscretePoint>> successors = new ArrayList<>();
		for (Direction d : directionsFor) {
			if(d.name.equals("UP") || d.name.equals("DOWN")) {
				limit = playfield.height;
			} else if(d.name.equals("LEFT") || d.name.equals("RIGHT")) {
				limit = playfield.height;
			}
			List<DiscretePoint> pointsInThatDirection = new ArrayList<>();
			
			int deeepness = 0;
			for(DiscretePoint point = p;
					playfield.get(point).canBeWalkedOn() && deeepness<=limit;
					point = playfield.putBackOnPlayground(d.move(point))
					) {
				pointsInThatDirection.add(point);
				deeepness++;
			}
			// Since we always include this point, we have to make sure there is at least one other point 
			if(pointsInThatDirection.size()>1) {
				successors.add(pointsInThatDirection);
			}
		}
		return successors;
	}

	private Playground<List<Direction>> computePossibleDirections(Playfield playfield, List<DiscretePoint> locations) {
		Playground<List<Direction>> returned = new Playground<>(playfield.getWidth(), playfield.getHeight());
		for(DiscretePoint point : locations) {
			List<Direction> directionsForPoint = new ArrayList<>();
			for (Direction d : Direction.DIRECTIONS) {
				DiscretePoint next = playfield.putBackOnPlayground(d.move(point));
				if (playfield.get(next).canBeWalkedOn()) {
					directionsForPoint.add(d);
				}
			}
			returned.set(point, directionsForPoint);
		}
		return returned;
	}

	private Playground<Integer> computeDistancesTo(Playfield playfield, DiscretePoint c, int limit) {
		Playground<Integer> distances = new Playground<>(playfield.width, playfield.height, EvolvableConstants.DISTANCE_UNREACHABLE);
		List<DiscretePoint> points = Arrays.asList(c);
		int distance = 0;
		while (!points.isEmpty() && distance <= limit) {
			List<DiscretePoint> nextRound = new LinkedList<>();
			// First, set value in all known points
			for (DiscretePoint p : points) {
				distances.set(p, distance);
				// For each point, observe the list of neighbors which distance is greater than
				// distance + 1, and loop on them
				for (Direction d : Direction.DIRECTIONS) {
					DiscretePoint next = playfield.putBackOnPlayground(d.move(p));
					if (playfield.allow(next)) {
						if (distances.get(next) > distance + 1) {
							nextRound.add(next);
						}
					}
				}
			}
			distance++;
			points = nextRound;
		}
		return distances;
	}

	ImmutablePlayground<Double> usingDistance(DiscretePoint system) {
		return distancesToPoints.get(system).distancesOnPlaygroundSquared;
	}

	public SortedSet<DiscretePoint> getNearestPoints(DiscretePoint point) {
		if(nearestPoints.get(point)==null) {
			SortedSet<DiscretePoint> sorted = new TreeSet<>(new ByDistanceOnPlayground(point, usingDistance(point)));
			sorted.addAll(locations);
			nearestPoints.set(point, sorted);
			
		}
		return nearestPoints.get(point);
	}


}
