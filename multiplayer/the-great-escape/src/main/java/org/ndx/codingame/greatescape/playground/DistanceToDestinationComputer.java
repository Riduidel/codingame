package org.ndx.codingame.greatescape.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ndx.codingame.greatescape.entities.Gamer;
import org.ndx.codingame.greatescape.entities.Wall;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class DistanceToDestinationComputer {
	private final Gamer destination;

	public DistanceToDestinationComputer(final Gamer gamer) {
		destination = gamer;
	}

	public DistanceInfo computeOn(final Playfield playfield) {
		final DiscretePoint targetPoint = playfield.toPlayfieldPositionForContent(destination.x, destination.y);
		final DistanceInfo returned = new DistanceInfo(playfield.width, playfield.height);
		 Collection<DiscretePoint> points = initialzePointsFor(destination, playfield);
		int iteration = 1;
		while(returned.get(targetPoint)==DistanceInfo.TOO_FAR && iteration<DistanceInfo.TOO_FAR) {
			points = computeOneMoreIteration(playfield, returned, points, iteration++);
		}
		return returned;
	}

	private Collection<DiscretePoint> computeOneMoreIteration(final Playfield playfield, final DistanceInfo returned,
			final Collection<DiscretePoint> points, final int i) {
		return points.stream()
			.flatMap((p)->setDistanceValueAtAndReturnNext(playfield, returned, p, i).stream())
			.collect(Collectors.toList());
	}

	private Collection<DiscretePoint> setDistanceValueAtAndReturnNext(final Playfield playfield, final DistanceInfo distanceInfo, final DiscretePoint position, final int i) {
		if(i<distanceInfo.get(position)) {
			if(!Wall.class.isInstance(playfield.get(position))) {
				distanceInfo.set(position, Math.min(i, distanceInfo.get(position)));
				final List<DiscretePoint> returned = new ArrayList<>();
				for(final Direction direction : Direction.DIRECTIONS) {
					final DiscretePoint next = direction.move(position);
					if(distanceInfo.contains(next)) {
						returned.add(next);
					}
				}
				return returned;
			}
		}
		return Collections.emptyList();
	}

	private Collection<DiscretePoint> initialzePointsFor(final Gamer destination, final Playfield playfield) {
		final int x = destination.direction.equals(Direction.LEFT) ? 0: playfield.width-1;
		return IntStream.range(0, playfield.height)
			.filter((i)->i%2==0)
			.mapToObj((i)->new DiscretePoint(x, i))
			.collect(Collectors.toList());
	}
}
