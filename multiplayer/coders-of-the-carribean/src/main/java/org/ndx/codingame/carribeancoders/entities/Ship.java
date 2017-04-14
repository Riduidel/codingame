package org.ndx.codingame.carribeancoders.entities;

import java.util.SortedMap;
import java.util.TreeMap;

import org.ndx.codingame.carribeancoders.actions.MoveTo;
import org.ndx.codingame.carribeancoders.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class Ship extends Entity {
	public final int speed;
	public final int rum;
	public final int orientation;
	public final int owner;
	public Ship(final int x, final int y, final int arg1, final int arg2, final int arg3, final int arg4) {
		this(new DiscretePoint(x, y), arg1, arg2, arg3, arg4);
	}
	public Ship(final DiscretePoint discretePoint, final int arg1, final int arg2, final int arg3, final int arg4) {
		super(discretePoint);
		orientation = arg1;
		speed = arg2;
		rum = arg3;
		owner = arg4;
	}
	
	public Action computeMove(final Playfield playfield) {
		// Move to nearest barrel 
		final SortedMap<DiscretePoint, Barrel> byDistance = new TreeMap<>(new AbstractPoint.PositionByDistance2To(position));
		playfield.getBarrels().stream()
			.forEach((b) -> byDistance.put(b.position, b));
		return new MoveTo(byDistance.firstKey());
	}
	
	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = super.toUnitTestConstructorPrefix(multilinePrefix);
		returned
			.append(orientation).append(", ")
			.append(speed).append(", ")
			.append(rum).append(", ")
			.append(owner).append(")")
			;
		return returned;
	}
}