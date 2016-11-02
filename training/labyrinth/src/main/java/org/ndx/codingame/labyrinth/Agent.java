package org.ndx.codingame.labyrinth;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class Agent {
	public static final int RANGE = 5;

	public DiscretePoint position;
	
	private Strategy strategy = new Lookup();

	private MoveTo backHome;
	private MoveTo toControlCenter;
	private Deque<MoveTo> fallbacks = new ArrayDeque<>();

	private int timeToComeBack;

	public Agent() {
		this(Integer.MAX_VALUE);
	}
	public Agent(int timeToComeBack) {
		this.timeToComeBack = timeToComeBack;
	}

	public void putOn(int x, int y) {
		this.position = new DiscretePoint(x, y);
	}

	public Direction moveOn(PlayField playground) {
		return strategy.move(this, playground);
	}

	public void scan(final PlayField playground, final Direction move) {
		if(move==null) {
			/* add fallback strategy for each undiscoevred reachable point in RANGE */
			handleFallbackPoints(playground, position);
			System.err.println("Instanciated a back to home strategy");
			backHome = new MoveTo(playground, position);
		} else {
			/* add fallback strategy for each undiscoevred reachable point in RANGE */
			handleFallbackPoints(playground, move);
			strategies().stream().forEach(s -> s.extend(playground));
			strategy = mutate(playground, move);
		}
	}
	private Strategy mutate(PlayField playground, final Direction move) {
		if(toControlCenter==null) {
			DiscretePoint controlCenter = findControlCenterAround(playground, move);
			if(controlCenter!=null) {
				System.err.println(String.format("Found control center at %s", controlCenter));
				toControlCenter = new MoveTo(playground, controlCenter);
			}
		}
		if(playground.get(move)=='C') {
			System.err.println("All conditions are met. We can go back home");
			return backHome;
		}
		if(strategy!=backHome) {
			if(toControlCenter!=null) {
				int distanceToBase = backHome.distanceToDestination(playground, toControlCenter.destination);
				System.err.println(String.format("Control center is at %d from base, and maximum distance is %d", distanceToBase, timeToComeBack));
				if(distanceToBase<=timeToComeBack) {
					int distanceToControlCenter = toControlCenter.distanceToDestination(playground, move);
					System.err.println("Home is at good destination from control center. We can go to control center. Control center is at "+distanceToControlCenter);
					if(distanceToControlCenter<toControlCenter.heatmap.MAX_DISTANCE) {
						System.err.println("And we know a valid path to control center. GO !");
						return toControlCenter;
					} else {
						System.err.println("But we don't know a path to control center");
					}
				}
			}
		}
		return strategy.mutate(move);
	}
	
	DiscretePoint findControlCenterAround(PlayField playground, DiscretePoint move) {
		for(int x=-RANGE+move.x; x<=RANGE+move.x;x++) {
			for(int y=-RANGE+move.y; y<=RANGE+move.y;y++) {
				if(playground.contains(x, y)) {
					if(playground.get(x, y)=='C') {
						return new DiscretePoint(x, y);
					}
				}
			}
		}
		return null;
	}
	private void handleFallbackPoints(final PlayField playground, final DiscretePoint move) {
		fallbacks = filterValid(playground);
		System.err.println(String.format("We have %s fallback locations", fallbacks.size()));
		Collection<DiscretePoint> fallbackPoints = allStrategies().stream()
				.map(s -> s.destination)
				.collect(Collectors.toSet());
		Collection<DiscretePoint> newFallbacks = findFallbackPointsIn(playground, move, fallbackPoints, new ArrayDeque<>(), 0);
		if(!newFallbacks.isEmpty()) {
			System.err.println("Found new fallback points\n"+newFallbacks);
		}
		newFallbacks.stream().forEach(p -> fallbacks.add(new MoveTo(playground, p)));
	}
	private Deque<MoveTo> filterValid(final PlayField playground) {
		Deque<MoveTo> valid = new ArrayDeque<>();
		for(MoveTo previous : fallbacks) {
			if(isInteresting(playground, previous)) {
				valid.addLast(previous);
			} else {
				System.err.println(previous.destination+" is no more interesting");
			}
		}
		return valid;
	}

	private boolean isInteresting(PlayField playground, MoveTo previous) {
		for(Direction d : Direction.DIRECTIONS) {
			ScoredDirection move = d.move(previous.destination);
			if(playground.contains(move)) {
				if(playground.get(move)=='?') {
					return true;
				}
			}
		}
		return false;
	}
	public Collection<DiscretePoint> findFallbackPointsIn(PlayField playfield, DiscretePoint point,
			Collection<DiscretePoint> excluding, Deque<DiscretePoint> knownPoints, int deepness) {
		if(deepness>RANGE*2) {
			return Collections.emptyList();
		} else if(excluding.contains(point)) {
			return Collections.emptyList();
		} else if(knownPoints.contains(point)) {
			return Collections.emptyList();
		} else {
			if(playfield.contains(point)) {
				char value = playfield.get(point);
				switch(value) {
				case '#':
					return Collections.emptyList();
				case '?':
					// If this point is unknown, move to previous one, the last in knownPoints
					return Arrays.asList(knownPoints.getLast());
				}
				knownPoints.add(point);
				Collection<DiscretePoint> returned = new HashSet<>();
				for (Direction d : Direction.DIRECTIONS) {
					DiscretePoint moved = d.move(point);
					returned.addAll(findFallbackPointsIn(playfield, moved, excluding, knownPoints, deepness+1));
				}
				knownPoints.remove(point);
				return returned;
			} else {
				return Collections.emptyList();
			}
		}
	}

	public Collection<MoveTo> allStrategies() {
		Collection<MoveTo> strategies = new ArrayList<>();
		if(backHome!=null)
			strategies.add(backHome);
		if(toControlCenter!=null)
			strategies.add(toControlCenter);
		strategies.addAll(fallbacks);
		return strategies;
	}

	public Collection<MoveTo> strategies() {
		Collection<MoveTo> strategies = new ArrayList<>();
		strategies.add(backHome);
		if(toControlCenter!=null)
			strategies.add(toControlCenter);
		if(!fallbacks.isEmpty())
			strategies.add(fallbacks.getLast());
		return strategies;
	}

	public Direction moveUsingFallback(PlayField playground) {
		strategy = findFallbackFor(playground, this.position);
		System.err.println("Now using strategy "+strategy+" with heatmap\n"+((MoveTo)strategy).heatmap);
		return strategy.move(this, playground);
	}

	private Strategy findFallbackFor(PlayField playground, DiscretePoint position) {
		// if none match, rebuild latest 
		Comparator<MoveTo> comparator = Comparator
				.comparingInt((MoveTo m) -> Lookup.score(m.destination, playground, 0, 0, new HashSet<>()))
				.reversed(); 
		SortedSet<MoveTo> sortedFallbacks= new TreeSet<>(comparator);
		sortedFallbacks.addAll(fallbacks);
		MoveTo last = sortedFallbacks.first();
		System.err.println("Latest fallback value for position "+last.heatmap.get(position));
		if(last.heatmap.get(position)>=last.heatmap.MAX_DISTANCE) {
			System.err.println("Latest fallback do not know how to handle position "+position+". Rebuilding.");
			last = new MoveTo(playground, last.destination, -1);
		}
		return last;
	}
	@Override
	public String toString() {
		return "Agent [position=" + position + ", strategy=" + strategy + "]";
	}
}
