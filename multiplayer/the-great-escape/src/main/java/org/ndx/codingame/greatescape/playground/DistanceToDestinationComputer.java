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
	private static final int TOO_FAR = 1000;
	private final Gamer gamer;

	public DistanceToDestinationComputer(final Gamer gamer) {
		this.gamer = gamer;
	}

	public DistanceInfoPlayground computeOn(final Playfield playfield) {
		final DiscretePoint targetPoint = playfield.toPlayfieldPositionForContent(gamer.x, gamer.y);
		final DistanceInfoPlayground returned = new DistanceInfoPlayground(playfield.width, playfield.height);
		 Collection<DiscretePoint> points = initializePointsFor(gamer.direction, playfield);
		int iteration = 1;
		while(returned.get(targetPoint)==null && iteration<TOO_FAR) {
			points = computeOneMoreIteration(playfield, returned, points, iteration++);
		}
		return returned;
	}

	private Collection<DiscretePoint> computeOneMoreIteration(final Playfield playfield, final DistanceInfoPlayground returned,
			final Collection<DiscretePoint> points, final int i) {
		return points.stream()
			.flatMap((p)->setDistanceValueAtAndReturnNext(playfield, returned, p, i).stream())
			.collect(Collectors.toList());
	}

	private Collection<DiscretePoint> setDistanceValueAtAndReturnNext(final Playfield playfield, final DistanceInfoPlayground distanceInfo, final DiscretePoint position, final int i) {
		final DistanceInfo distanceInfoAt = distanceInfo.getOrCreate(position);
		if(distanceInfoAt.getDistance()>i) {
			if(!Wall.class.isInstance(playfield.get(position))) {
				distanceInfoAt.minimizeDistanceTo(i);
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

	private Collection<DiscretePoint> initializePointsFor(final Direction direction, final Playfield playfield) {
		if(direction.y==0) {
			final int x = direction.equals(Direction.LEFT) ? 0: playfield.width-1;
			return IntStream.range(0, playfield.height)
				.filter((i)->i%2==0)
				.mapToObj((i)->new DiscretePoint(x, i))
				.collect(Collectors.toList());
		} else {
			final int y = direction.equals(Direction.UP) ? 0: playfield.height-1;
			return IntStream.range(0, playfield.width)
				.filter((i)->i%2==0)
				.mapToObj((i)->new DiscretePoint(i, y))
				.collect(Collectors.toList());
		}
	}
}
