package org.ndx.codingame.spring.challenge.playground;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.AbstractDistinctContent;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.SmallPill;

public class Playfield extends Playground<Content> implements SpringChallengePlayground {
	private Map<BigPill, ScoringSystem> bigPillsInfos = new HashMap<>();
	private Playground<Integer> zero;
	private Playground<List<DiscretePoint>> nextPointsForNormal;
	private Playground<List<List<DiscretePoint>>> nextPointsForSpeed;

	public Playfield(final int width, final int height) {
		super(width, height);
	}

	
	/**
	 * Cloning constructor
	 * @param playground
	 */
	public Playfield(final Playfield playground) {
		super(playground);
	}
	
	public void init() {
		// Force init of zero !
		zero();
		// Compute valid directions for each point
		Playground<List<Direction>> directions = computePossibleDirections();
		nextPointsForNormal = computeNextPointsForNormal(directions);
		nextPointsForSpeed = computeNextPointsForSpeed(directions);
	}
	
	private Playground<List<DiscretePoint>> computeNextPointsForNormal(Playground<List<Direction>> directions) {
		Playground<List<DiscretePoint>> points = new Playground<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				List<Direction> directionsFor = directions.get(x, y);
				DiscretePoint p = new DiscretePoint(x, y);
				List<DiscretePoint> successors = new LinkedList<>();
				for(Direction d : directionsFor) {
					DiscretePoint next = putBackOnPlayground(d.move(p));
					if(get(next).canBeWalkedOn()) {
						successors.add(next);
					}
				}
				points.set(p, successors);
			}
		}
		return points;
	}
	
	private Playground<List<List<DiscretePoint>>> computeNextPointsForSpeed(Playground<List<Direction>> directions) {
		Playground<List<List<DiscretePoint>>> points = new Playground<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				List<Direction> directionsFor = directions.get(x, y);
				DiscretePoint p = new DiscretePoint(x, y);
				List<List<DiscretePoint>> successors = new LinkedList<>();
				for(Direction d : directionsFor) {
					DiscretePoint first = putBackOnPlayground(d.move(p));
					if(get(first).canBeWalkedOn()) {
						DiscretePoint second = putBackOnPlayground(d.move(first));
						if(get(second).canBeWalkedOn()) {
							successors.add(Arrays.asList(second));
						}
					}
				}
				points.set(p, successors);
			}
		}
		return points;
	}


	private Playground<List<Direction>> computePossibleDirections() {
		Playground<List<Direction>> directions = new Playground<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				List<Direction> directionsForPoint = new LinkedList<>();
				DiscretePoint point = new DiscretePoint(x, y);
				for(Direction d : Direction.DIRECTIONS) {
					DiscretePoint next = putBackOnPlayground(d.move(point));
					if(get(next).canBeWalkedOn()) {
						directionsForPoint.add(d);
					}
				}
				directions.set(point, directionsForPoint);
			}
		}
		return directions;
	}


	@Override public ScoringSystem cacheDistanceMapTo(BigPill c) {
		if(!bigPillsInfos.containsKey(c)) {
			bigPillsInfos.put(c, computeScoringInfos(c, EvolvableConstants.HORIZON_FOR_BIG_PILLS));
		}
		return bigPillsInfos.get(c);
	}

	protected ScoringSystem computeScoringInfos(AbstractDistinctContent c, int limit) {
		return new ScoringSystem(c, computeDistancesTo(c, limit));
	}


	private Playground<Integer> computeDistancesTo(DiscretePoint c, int limit) {
		Playground<Integer> distances = new Playground<>(width, height, EvolvableConstants.DISTANCE_UNREACHABLE);
		List<DiscretePoint> points = Arrays.asList(c);
		int distance = 0;
		while(!points.isEmpty() && distance<=limit) {
			List<DiscretePoint> nextRound = new LinkedList<>();
			// First, set value in all known points
			for(DiscretePoint p : points) {
				distances.set(p, distance);
				// For each point, observe the list of neighbors which distance is greater than distance + 1, and loop on them
				for(Direction d : Direction.DIRECTIONS) {
					DiscretePoint next = putBackOnPlayground(d.move(p));
					if(allow(next)) {
						if(distances.get(next)>distance+1) {
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

	@Override
	public Turn readWriteProxy() {
		return new Turn(this);
	}

	@Override public Playground<Integer> zero() {
		if(zero==null) {
			zero = new Playground<Integer>(width, height, 0);
		}
		return zero;
	}

	public List<List<DiscretePoint>> speedPointsAt(DiscretePoint p) {
		return nextPointsForSpeed.get(p);
	}


	public List<DiscretePoint> nextPointsAt(DiscretePoint p) {
		return nextPointsForNormal.get(p);
	}
}
