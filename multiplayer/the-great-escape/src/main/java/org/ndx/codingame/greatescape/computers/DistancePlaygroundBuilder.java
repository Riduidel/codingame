package org.ndx.codingame.greatescape.computers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ndx.codingame.greatescape.entities.Gamer;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class DistancePlaygroundBuilder {

	private final Gamer gamer;

	public DistancePlaygroundBuilder(final Gamer gamer) {
		this.gamer = gamer;
	}

	public DistancePlayground computeOn(final Playfield tested) {
		final DistancePlayground returned = new DistancePlayground(tested);
		// First, compute distance map
		fillDistancesTo(returned, tested, Arrays.asList(gamer.toPlayfieldPosition()), 0);
		// Then build trajectories to have best direction immediatly accessible
		buildTrajectoriesFrom(returned, gamer);
		return returned;
	}

	/**
	 * Build trajectories by getting each point from the direction of the gamer
	 * @param returned 
	 * @param gamer2
	 */
	private void buildTrajectoriesFrom(final DistancePlayground returned, final Gamer gamer) {
		final Collection<DiscretePoint> start = findStartPoints(returned, gamer.direction);
		// Update reverse distances from destinations
		start.stream()
			.forEach(point -> returned.get(point).setReverseDistance(0));
		buildTrajectoriesFrom(returned, gamer.toPlayfieldPosition(), start);
	}

	private void buildTrajectoriesFrom(final DistancePlayground returned, final DiscretePoint playfieldPosition,
			Collection<DiscretePoint> points) {
		while(!points.contains(playfieldPosition)) {
			points = buildTrajectoriesFromPointsIn(returned, points);
		}
	}

	private Collection<DiscretePoint> buildTrajectoriesFromPointsIn(final DistancePlayground returned,
			final Collection<DiscretePoint> start) {
		return start.stream()
				.map(point -> findBestPredecessor(returned, point))
				.filter(point -> point!=null)
				.collect(Collectors.toSet());
	}

	private DiscretePoint findBestPredecessor(final DistancePlayground returned, final DiscretePoint point) {
		 final Map<Integer, List<ScoredDirection<?>>> scores = Direction.DIRECTIONS.stream()
			.map(direction -> direction.move(point))
			.filter(direction -> returned.contains(direction))
			.collect(Collectors.groupingBy((direction) -> returned.getDistance(direction)));
		final Integer shortest = new TreeSet<>(scores.keySet()).first();
		final DiscretePoint position = scores.get(shortest).get(0);
		returned.get(position).addPathFrom(returned, point);
		return position;
	}

	private Collection<DiscretePoint> findStartPoints(final DistancePlayground returned, final Direction direction) {
		if(direction.getY()==0) {
			final int x = direction.x<0 ? 0 : returned.width-1;
			return IntStream.rangeClosed(0, returned.height)
				.filter(y -> y%2==0)
				.mapToObj(y -> new DiscretePoint(x, y))
				.collect(Collectors.toList());
		} else {
			final int y = direction.y<0 ? 0 : returned.height-1;
			return IntStream.rangeClosed(0, returned.width)
					.filter(x -> x%2==0)
					.mapToObj(x -> new DiscretePoint(x, y))
					.collect(Collectors.toList());
		}
	}

	private void fillDistancesTo(final DistancePlayground returned, final Playfield tested, Collection<DiscretePoint> asList, int i) {
		while(!asList.isEmpty()) {
			asList = fillDistancesToPointsIn(returned, tested, asList, i++);
		}
	}

	private Set<DiscretePoint> fillDistancesToPointsIn(final DistancePlayground returned, final Playfield playfield, final Collection<DiscretePoint> isoDistance, final int distance) {
		isoDistance.stream()
			.forEach(point -> returned.setDistance(point, distance));
		final int nextDistance = distance+1;
		final Set<DiscretePoint> children = isoDistance.stream()
			.map(point -> findEmptyNeighbours(returned, playfield, point, nextDistance))
			.flatMap(Collection::stream)
			.collect(Collectors.toSet());
		return children;
	}

	private List<DiscretePoint> findEmptyNeighbours(final DistancePlayground distances, final Playfield playfield, final DiscretePoint point, final int distance) {
		final List<DiscretePoint> returned = new ArrayList<>(4);
		for(final Direction d : Direction.DIRECTIONS) {
			final ScoredDirection<Object> moved = d.move(point);
			if(playfield.contains(moved)) {
				if(!playfield.isWall(moved)) {
					if(distances.getDistance(moved)>distance) {
						returned.add(moved);
					}
				}
			}
		}
		return returned;
	}
}
