package org.ndx.codingame.gameofdrones.entities;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ndx.codingame.gameofdrones.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.libgaming2d.MoveToPoint;

public class Drone implements ConstructableInUnitTest {
	public static final int SPEED = 100;
	public final int owner;
	public final ContinuousPoint position;
	public final int number;
	public final ContinuousPoint previous;
	private Zone zone;
	public Drone(final double dX, final double dY, final int owner, final int j) {
		this(dX, dY, dX, dY, owner, j);
	}
	public Drone(final double x, final double y, final double dX, final double dY, final int owner, final int j) {
		position = new ContinuousPoint(x, y);
		previous = new ContinuousPoint(dX, dY);
		this.owner = owner;
		number = j;
	}

	public Action findDestination(final Playfield playfield) {
		return new MoveToPoint(playfield.getZones().stream()
				.map(zone -> zone.circle.center)
				.sorted(new AbstractPoint.PositionByDistance2To(position))
				.findFirst()
				.orElseGet(() -> position));
	}
	
	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix)  {
		final StringBuilder returned = new StringBuilder();
		returned.append("new ").append(getClass().getSimpleName()).append("(")
			.append(position.x).append(", ")
			.append(position.y).append(", ")
			.append(previous.x).append(", ")
			.append(previous.y).append(", ")
			.append(owner).append(", ")
			.append(number).append(")");
		return returned;
	}

	public Drone mapToPrevious(final Map<Object, Map<Object, Optional<Drone>>> previousTurn) {
		if(previousTurn.containsKey(owner)) {
			final Map<Object, Optional<Drone>> previousTurnForOwner = previousTurn.get(owner);
			if(previousTurnForOwner.containsKey(number)) {
				final Optional<Drone> previousTurnForThisDrone = previousTurnForOwner.get(number);
				final Drone previous = previousTurnForThisDrone.orElse(this);
				return new Drone(position.x, position.y, previous.position.x, previous.position.y, owner, number);
			}
		}
		return this;
	}
	public void findContainingZone(final List<Zone> zones) {
		zones.stream().forEach((z) -> z.addContainedDroneIfNearby(this));
	}
	public void setZone(final Zone zone) {
		this.zone = zone;
	}
}
