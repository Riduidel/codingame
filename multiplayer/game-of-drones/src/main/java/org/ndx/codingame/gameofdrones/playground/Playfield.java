package org.ndx.codingame.gameofdrones.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ndx.codingame.gameofdrones.entities.Drone;
import org.ndx.codingame.gameofdrones.entities.Zone;
import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.gaming.tounittest.ToUnitTestStringBuilder;

public class Playfield implements ToUnitTestFiller {
	private int owner;
	private final List<Zone> zones = new ArrayList<>();
	private final List<Drone> drones = new ArrayList<>();
	
	public String toUnitTestString() {
		return new ToUnitTestStringBuilder("can_find_moves").build(this);
	}

	@Override
	public StringBuilder build() {
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
		// before computing moves, check which zone owns which drone
		drones.forEach((d)->d.findContainingZone(zones));
		return drones.stream()
			.filter((d) -> d.owner==owner)
			.map(d -> d.findDestination(this))
			.map((a) -> a.toCommandString())
			.collect(Collectors.joining("\n"));
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
	
}
