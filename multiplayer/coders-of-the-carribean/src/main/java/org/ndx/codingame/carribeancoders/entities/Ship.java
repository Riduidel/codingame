package org.ndx.codingame.carribeancoders.entities;

import java.util.SortedMap;
import java.util.TreeMap;

import org.ndx.codingame.carribeancoders.actions.MoveTo;
import org.ndx.codingame.carribeancoders.actions.ShootAt;
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
			return attackEnemy(nearestEnemyPosition);
		} else if(rum<55) {
			return new MoveTo(nearestBarrelPosition);
		} else if(rum>80) {
			return attackEnemy(nearestEnemyPosition);
		} else {
			return new MoveTo(nearestBarrelPosition);
		}
		
	}
	private Action attackEnemy(final DiscretePoint nearestEnemyPosition) {
		if(nearestEnemyPosition.distance2To(position)<5) {
			return new ShootAt(nearestEnemyPosition);
		} else {
			return new MoveTo(nearestEnemyPosition);
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