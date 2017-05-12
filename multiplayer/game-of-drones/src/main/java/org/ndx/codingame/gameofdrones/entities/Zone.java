package org.ndx.codingame.gameofdrones.entities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.base.Distance2;
import org.ndx.codingame.lib2d.shapes.Circle;

public class Zone implements ConstructableInUnitTest {
	public static class PositionByDistance2To implements Comparator<Zone> {
		private final AbstractPoint.PositionByDistance2To positionComparator;
		
		public PositionByDistance2To(final Distance2... principal) {
			positionComparator = new AbstractPoint.PositionByDistance2To(principal);
		}

		@Override
		public int compare(final Zone o1, final Zone o2) {
			return positionComparator.compare(o1.circle.center, o2.circle.center);
		}

	}

	public static final int RADIUS = 100;
	
	public final Circle circle;

	public final int owner;
	private final Map<Integer, Integer> ownersToDrones;

	public Zone(final int x, final int y) {
		this(Geometry.from(x, y).cirleOf(RADIUS));
	}

	public Zone(final Circle circle2) {
		this(circle2, 0);
	}
	public Zone(final Circle circle2, final int owner2) {
		this(circle2, owner2, Collections.emptyMap());
	}

	public Zone(final Circle circle2, final int owner2, final Map<Integer, Integer> mapping) {
		circle = circle2;
		owner = owner2;
		ownersToDrones = mapping;
	}

	public Zone(final int x, final int y, final int owner) {
		this(Geometry.from(x, y).cirleOf(RADIUS), owner);
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix)  {
		final StringBuilder returned = new StringBuilder();
		returned.append("new ").append(getClass().getSimpleName())
			.append("(").append((int) circle.center.x).append(", ").append((int) circle.center.y).append(", ").append(owner).append(")");
		return returned;
	}

	@Override
	public String toString() {
		return String.format("Zone [position=%s, owner=%s]", circle.center, owner);
	}

	public Zone derive(final List<Drone> nextDrones) {
		final Map<Integer, Integer> dronesByOwners = nextDrones.stream()
			.filter(d -> circle.includesOrContains(d.position))
			.collect(
				Collectors.groupingBy((d) -> d.owner,
					Collectors.summingInt((d) -> 1)));
		int ownerDrones = 0;
		if(dronesByOwners.containsKey(owner)) {
			ownerDrones = dronesByOwners.remove(owner);
		}
		int nextOwner = owner;
		// Now check which player has the most drones in
		for(final Map.Entry<Integer, Integer> entry : dronesByOwners.entrySet()) {
			if(entry.getValue()>ownerDrones) {
				ownerDrones = entry.getValue();
				nextOwner = entry.getKey();
			}
		}
		return new Zone(circle, nextOwner, dronesByOwners);
	}

	public Zone ownedBy(final int owner) {
		return new Zone(circle, owner);
	}
}
