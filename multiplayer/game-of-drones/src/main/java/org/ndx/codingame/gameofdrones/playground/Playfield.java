package org.ndx.codingame.gameofdrones.playground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ndx.codingame.gameofdrones.entities.Drone;
import org.ndx.codingame.gameofdrones.entities.Zone;
import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.gaming.tounittest.ToUnitTestStringBuilder;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.libgaming2d.MoveToPoint;

public class Playfield implements ToUnitTestFiller {
	public static interface Dimension {
		public static final int MIN_X = 0;
		public static final int MIN_Y = 0;
		public static final int MAX_X = 4000;
		public static final int MAX_Y = 1800;
	}
	private int owner;
	private final List<Zone> zones = new ArrayList<>();
	private final List<Drone> drones = new ArrayList<>();

	/**
	 * Override moves from standard derivation.
	 * This map is typically populated from {@link #computeMovesPerDrones()} actions.
	 */
	private final Map<Drone, MoveToPoint> derivationOverride = new HashMap<>();

	public Playfield() {

	}

	/**
	 * Test constructor streamlining most of construction operations
	 * @param zones
	 * @param drones
	 */
	public Playfield(final int owner, final List<Zone> zones, final List<Drone> drones) {
		this();
		this.owner = owner;
		addAllZones(zones);
		addAllDrones(drones);
	}

	public String toUnitTestString(final String effectiveCommand) {
		return new ToUnitTestStringBuilder("can_find_moves").build(this, effectiveCommand);
	}

	@Override
	public StringBuilder build(final String effectiveCommand) {
		final StringBuilder returned = new StringBuilder();
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, zones, List.class, Zone.class, "z"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, drones, List.class, Drone.class, "d"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield playfield = new Playfield();\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllZones(z);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllDrones(d);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.setOwner(").append(owner).append(");\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("assertThat(playfield.computeMoves()).isNotEmpty();\n");
		return returned;
	}

	public void addAllZones(final List<Zone> toAdd) {
		zones.addAll(toAdd);
	}

	public void addZone(final Zone ownedBy) {
		zones.add(ownedBy);
	}

	public void addAllDrones(final List<Drone> toAdd) {
		drones.addAll(toAdd);
	}
	public void addDrone(final Drone drone) {
		drones.add(drone);
	}

	/**
	 * For each drone, output a command
	 * @return
	 */
	public String computeMoves() {
		final Map<Drone, MoveToPoint> actionPerDrone = computeMovesPerDrones();
		return actionPerDrone.values().stream()
				.map(a -> a.toCommandString())
				.collect(Collectors.joining("\n"));
	}


	Map<Drone, MoveToPoint> computeMovesPerDrones() {
		for(final Drone d : drones) {
			if(d.owner==owner) {
				derivationOverride.put(d, computeMoveFor(d));
			}
		}
		return derivationOverride;
	}

	/**
	 * For each drone, we look for the nearest non owned zone (at time of arrival)
	 * @param d
	 * @return
	 */
	private MoveToPoint computeMoveFor(final Drone d) {
		final Optional<ContinuousPoint> byDistanceToDrone = zones.stream()
				.sorted(new Zone.PositionByDistance2To(d.position))
				// check if zone is owned at time of drone arrival
				.filter(z -> !owns(z, (int) d.position.distance2To(z.circle.center)/Drone.SPEED+1))
				.map(z -> z.circle.center)
				.findFirst();
		if(byDistanceToDrone.isPresent()) {
			return new MoveToPoint(byDistanceToDrone.get());
		} else {
			return new MoveToPoint(Geometry.at((Dimension.MAX_X-Dimension.MIN_X)/2, (Dimension.MAX_Y-Dimension.MIN_Y)/2));
		}
	}

	/**
	 * Check if the owner of this simulation owns the given zone at the given delay
	 * @param z
	 * @param derivation
	 * @return
	 */
	private boolean owns(final Zone zone, final int derivation) {
		Playfield current = this;
		for (int i = 0; i < derivation; i++) {
			current = current.derive();
		}
		final Zone future = current.zones.stream().filter(z -> z.circle.center.equals(zone.circle.center)).findFirst().get();
		return future.owner==owner;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public List<Drone> getDrones() {
		return drones;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(final int owner) {
		this.owner = owner;
	}

	public Map<Object, Map<Object, Optional<Drone>>> dronesToArray() {
		return drones.stream()
				.collect(
						Collectors.groupingBy((d) -> d.owner,
								Collectors.groupingBy((d) -> d.number,
										Collectors.reducing((a, b) -> a))));
	}

	public Playfield derive() {
		final List<Drone> nextDrones = drones.stream()
				.map(d -> d.derive(zones, Optional.ofNullable(derivationOverride.get(d))))
				.collect(Collectors.toList());
		final List<Zone> nextZones = zones.stream()
				.map(z -> z.derive(nextDrones))
				.collect(Collectors.toList());
		return new Playfield(owner, nextZones, nextDrones);
	}

	public Playfield deriveToHorizon() {
		if(isStabilized()) {
			return this;
		} else {
			return derive().deriveToHorizon();
		}
	}

	/**
	 * Simulation is stabilized when all drones stop moving
	 * @return
	 */
	private boolean isStabilized() {
		return drones.stream()
				.allMatch(d -> d.getVector().length()<1);
	}
}
