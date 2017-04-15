package org.ndx.codingame.carribeancoders.entities;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.ndx.codingame.carribeancoders.actions.MoveTo;
import org.ndx.codingame.carribeancoders.actions.ShootAt;
import org.ndx.codingame.carribeancoders.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class Ship extends Entity {
	private static final List<Direction> EVEN_OFFSETS = Arrays.asList(
			new Direction(1, 0, "right"),
			new Direction(0, -1, "top right"),
			new Direction(1, -1, "top left"),
			new Direction(-1, 0, "left"),
			new Direction(-1, 1, "down left"),
			new Direction(0, 1, "down right")
			);
	private static final List<Direction> ODD_OFFSETS = Arrays.asList(
			new Direction(1, 0, "right"),
			new Direction(1, -1, "top right"),
			new Direction(0, 1, "top left"),
			new Direction(-1, 0, "left"),
			new Direction(0, 1, "down left"),
			new Direction(1, 1, "down right")
			);
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
		final SortedMap<DiscretePoint, Barrel> barrelsByDistance = new TreeMap<>(new AbstractPoint.PositionByDistance2To(position));
		final SortedMap<DiscretePoint, Ship> enemiesByDistance = new TreeMap<>(new AbstractPoint.PositionByDistance2To(position));
		playfield.getBarrels().stream()
			.forEach((b) -> barrelsByDistance.put(b.position, b));
		playfield.getEnemyShips().stream()
			.forEach((s) -> enemiesByDistance.put(s.position, s));
		DiscretePoint nearestBarrelPosition = null;
		if(!barrelsByDistance.isEmpty()) {
			nearestBarrelPosition = barrelsByDistance.firstKey();
		}
		DiscretePoint nearestEnemyPosition = null;
		if(!enemiesByDistance.isEmpty()) {
			nearestEnemyPosition = enemiesByDistance.firstKey();
		}
		if(nearestBarrelPosition==null) {
			return attackEnemy(nearestEnemyPosition, enemiesByDistance.get(nearestEnemyPosition));
		} else if(rum<55) {
			return new MoveTo(nearestBarrelPosition);
		} else if(nearestEnemyPosition.distance2To(position)<nearestBarrelPosition.distance2To(position)) {
			return attackEnemy(nearestEnemyPosition, enemiesByDistance.get(nearestEnemyPosition));
		} else {
			return new MoveTo(nearestBarrelPosition);
		}
		
	}
	private Action attackEnemy(final DiscretePoint nearestEnemyPosition, final Ship ship) {
		if(nearestEnemyPosition.distance2To(position)<5) {
			// Take in account enemy speed to aim the correct position
			// This code is lame, and I feel bad for it, but I try it before diving into hex grid storage (which is quite complex)
			return shootAt(nearestEnemyPosition, ship.orientation, ship.speed);
		} else {
			return new MoveTo(nearestEnemyPosition);
		}
	}
	
	private Action shootAt(final DiscretePoint nearestEnemyPosition, final int orientation2, final int speed2) {
		if(speed2>0) {
			List<Direction> OFFSETS = null;
			switch(nearestEnemyPosition.y%2) {
			case 0:
				OFFSETS = EVEN_OFFSETS;
				break;
			case 1:
				OFFSETS = ODD_OFFSETS;
				break;
			}
			return shootAt(OFFSETS.get(orientation2).move(nearestEnemyPosition), orientation2, speed2-1);
		} else {
			return new ShootAt(nearestEnemyPosition);
		}
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
	@Override
	public <Type> Type accept(final EntityVisitor<Type> visitor) {
		return visitor.visitShip(this);
	}
}