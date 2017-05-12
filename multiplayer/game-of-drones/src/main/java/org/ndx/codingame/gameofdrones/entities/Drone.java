package org.ndx.codingame.gameofdrones.entities;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ndx.codingame.gameofdrones.playground.Playfield;
import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.lib2d.shapes.Vector;
import org.ndx.codingame.libgaming2d.MoveToPoint;

public class Drone implements ConstructableInUnitTest {
	public static final int SPEED = 100;
	public final int owner;
	public final ContinuousPoint position;
	public final int number;
	public final ContinuousPoint previous;
	private Vector vector;

	public Drone(final double dX, final double dY, final int owner, final int j) {
		this(dX, dY, dX, dY, owner, j);
	}
	public Drone(final double dX, final double dY, final double x, final double y, final int owner, final int number) {
		this(new ContinuousPoint(dX, dY), new ContinuousPoint(x, y), owner, number);
	}

	public Drone(final ContinuousPoint previous, final ContinuousPoint position, final int owner2, final int number2) {
		this.position = position;
		this.previous = previous;
		owner = owner2;
		number = number2;
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
				return new Drone(previous.position.x, previous.position.y, position.x, position.y, owner, number);
			}
		}
		return this;
	}
	@Override
	public String toString() {
		return String.format("Drone [position=%s, owner=%s, number=%s]", position, owner, number);
	}

	/**
	 * All drones are supposed to stop at center of destination zone, destination which is found from the {@link #previous} to {@link #position} segment
	 * @param zones
	 * @param optional
	 * @return
	 */
	public Drone derive(final List<Zone> zones, final Optional<MoveToPoint> overridenDestination) {
		if(previous.equals(position)) {
			return new Drone(previous, position, owner, number);
		} else {
			final Segment segment = getVector();
			ContinuousPoint next = segment.pointAtDistance(position, segment.length(), position);
			// Do not forget to handle case of drones navigating outside playfield
			if(next.x<Playfield.Dimension.MIN_X) {
				next = new ContinuousPoint(Playfield.Dimension.MIN_X, next.y);
			}
			if(next.x>Playfield.Dimension.MAX_X) {
				next = new ContinuousPoint(Playfield.Dimension.MAX_X, next.y);
			}
			if(next.y<Playfield.Dimension.MIN_Y) {
				next = new ContinuousPoint(next.x, Playfield.Dimension.MIN_Y);
			}
			if(next.y>Playfield.Dimension.MAX_Y) {
				next = new ContinuousPoint(next.x, Playfield.Dimension.MAX_Y);
			}
			if(overridenDestination.isPresent()) {
				final Segment s = Geometry.from(position).segmentTo((ContinuousPoint) overridenDestination.get().target);
				next = s.pointAtDistance(SPEED, position);
			} else {
				final Optional<Zone> destination = zones.stream().filter(z -> z.circle.includesOrContains(position)).findFirst();
				if(destination.isPresent()) {
					next = destination.get().circle.center;
				}
			}
			return new Drone(position, next, owner, number);
		}
	}
	public Vector getVector() {
		if(vector==null) {
			vector = Geometry.from(previous).vectorOf(position);
		}
		return vector;
	}
}
