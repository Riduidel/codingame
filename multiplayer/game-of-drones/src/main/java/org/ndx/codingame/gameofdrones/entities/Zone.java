package org.ndx.codingame.gameofdrones.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.shapes.Circle;

public class Zone implements ConstructableInUnitTest {
	public static final int RADIUS = 100;
	
	public final int owner;
	
	public final Circle circle;
	
	private int score = 0;
	
	private final List<Drone> drones = new ArrayList<>();

	/**
	 * Map each owner to the number of drones he has in this zone
	 */
	private Map<Integer, Integer> dronesByOwners;
	
	public Zone(final double x, final double y) {
		this(x, y, 0);
	}
	
	public Zone(final double x, final double y, final int i) {
		circle = Geometry.from(x, y).cirleOf(RADIUS);
		owner = i;
	}

	public Zone ownedBy(final int owner) {
		return new Zone(circle.center.x, circle.center.y, owner);
	}
	
	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix)  {
		final StringBuilder returned = new StringBuilder();
		returned.append("new ").append(getClass().getSimpleName()).append("(").append(circle.center.x).append(", ").append(circle.center.y).append(", ").append(owner).append(")");
		return returned;
	}

	public void addContainedDroneIfNearby(final Drone drone) {
		if(circle.includesOrContains(drone.position)) {
			drones.add(drone);
			drone.setZone(this);
			rescore();
		}
	}
	
	public void removeContainedDroneIfNearby(final Drone drone) {
		if(circle.includesOrContains(drone.position)) {
			drones.remove(drone);
			drone.setZone(null);
			rescore();
		}
	}

	private void rescore() {
		dronesByOwners = drones.stream()
			.collect(
				Collectors.groupingBy((d) -> d.owner,
					Collectors.summingInt((d) -> 1)));
		score = 0;
		dronesByOwners.entrySet().stream()
			.forEach((e) -> {
				if(e.getKey()==owner) {
					score += e.getValue();
				} else {
					score += e.getValue();
				}
			});
	}

	public int getScore() {
		return score;
	}
}
