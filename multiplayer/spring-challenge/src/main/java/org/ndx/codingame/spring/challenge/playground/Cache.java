package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Content;

public class Cache {
	public static final int NEXT_POINTS_VIEW_RANGE = Integer.MAX_VALUE;
	public static final int NEXT_POINTS_SPEED = 2;
	public static final int NEXT_POINTS_NORMAL = 1;
	public static final List<Integer> NEXT_POINTS_LIST = Arrays.asList(
			NEXT_POINTS_NORMAL,
			NEXT_POINTS_SPEED,
			NEXT_POINTS_VIEW_RANGE);

	private final Map<Integer, ImmutablePlayground<List<List<DiscretePoint>>>> nextPointsCache = new HashMap<>();
	private final ImmutablePlayground<ScoringSystem> distancesToPoints;
	public final ImmutablePlayground<List<Direction>> directions;
	private NearestPointCache nearestPoints;
	private List<DiscretePoint> locations;
	private final Playfield playfield;

	public Cache(Playfield playfield) {
		this.playfield = playfield;
		// Compute distance on playground for each point
		distancesToPoints = computeDistancesToPoints(playfield, getLocations());
		// Compute valid directions for each point
		directions = computePossibleDirections(playfield, getLocations());
		for(Integer i : NEXT_POINTS_LIST) {
			nextPointsCache.put(i, computeNextPointsList(playfield, directions, getLocations(), i));
		}
		nearestPoints = new NearestPointCache(this, playfield, getLocations());
	}
	
	public List<DiscretePoint> getLocations() {
		if(locations==null) {
			// Ccompute list of valid locations (in order to avoid browsing the whole table while walls don't interest us)
			locations = computeValidLocations(playfield);
		}
		return locations;
	}

	private List<DiscretePoint> computeValidLocations(Playfield playfield) {
		return Collections.unmodifiableList(playfield.accept(new PlaygroundAdapter<List<DiscretePoint>, Content>() {
			@Override
			public void startVisit(ImmutablePlayground<Content> playground) {
				returned = new ArrayList<>();
			}
			
			@Override
			public void visit(int x, int y, Content content) {
				/* I'm sorry, cause this one is absolutely not safe */
				if(playfield.allow(x, y, null)) {
					returned.add(new DiscretePoint(x, y));
				}
			}
		}));
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
				limit = Math.min(playfield.height, limit);
			} else if(d.name.equals("LEFT") || d.name.equals("RIGHT")) {
				limit = Math.min(playfield.width, limit);
			}
			List<DiscretePoint> pointsInThatDirection = new ArrayList<>();
			
			int deeepness = 0;
			for(DiscretePoint point = p;
					playfield.get(point).canBeWalkedOnBy(null) && deeepness<=limit;
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
				if (playfield.get(next).canBeWalkedOnBy(null)) {
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
					if (playfield.allow(next, null)) {
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

	public ImmutablePlayground<Double> usingDistanceTo(DiscretePoint system) {
		return distancesToPoints.get(system).distancesOnPlaygroundSquared;
	}

	public ImmutablePlayground<List<List<DiscretePoint>>> getNextPointsCache(int nextPointsViewRange) {
		return nextPointsCache.get(nextPointsViewRange);
	}

	public List<List<DiscretePoint>> getNextPointsCache(AbstractPac my) {
		return  getNextPointsCache(my.speedTurnsLeft > 0 ? Cache.NEXT_POINTS_SPEED : Cache.NEXT_POINTS_NORMAL).get(my);
	}

	public void loadPointsByDistanceUntil(Delay delay, int limit) {
		nearestPoints.loadPointsByDistanceUntil(delay, limit);
	}

	public boolean nearestPointsLoaded() {
		return nearestPoints.loaded();
	}

	public SortedSet<DiscretePoint> getPointsSortedByDistanceTo(DiscretePoint point) {
		return nearestPoints.getPointsSortedByDistanceTo(point);
	}
}
