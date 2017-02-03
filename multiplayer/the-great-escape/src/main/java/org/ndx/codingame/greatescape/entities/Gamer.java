package org.ndx.codingame.greatescape.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.greatescape.actions.Action;
import org.ndx.codingame.greatescape.actions.MoveTo;
import org.ndx.codingame.greatescape.playground.DistanceInfo;
import org.ndx.codingame.greatescape.playground.DistanceInfoPlayground;
import org.ndx.codingame.greatescape.playground.DistanceToDestinationComputer;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class Gamer extends DiscretePoint implements GameElement, Comparable<Gamer> {
	private final int wallsLeft;
	public final Direction direction;
	private DistanceInfoPlayground distanceMap;
	private DiscretePoint inPlayfield;
	private DistanceInfo distance;
	private DiscretePoint playfieldPostion;

	public Gamer(final int x, final int y, final int wallsLeft, final Direction direction) {
		super(x, y);
		this.wallsLeft = wallsLeft;
		this.direction = direction;
	}

	@Override
	public <Returned> Returned accept(final GameElementVisitor<Returned> visitor) {
		return visitor.visitGamer(this);
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder("new Gamer(");
		returned.append(x).append(", ");
		returned.append(y).append(", ");
		returned.append(wallsLeft).append(", ");
		returned.append(direction.toUnitTestConstructor(multilinePrefix)).append(")");
		return returned;
	}

	public Action compute(final Playfield tested) {
		// First step, obtain for each gamer the distance
		tested.getGamers().stream().forEach((g) -> g.computeDistanceToDestination(tested));
		final List<Gamer> enemies = tested.getGamers().stream()
				.filter((g)-> !g.equals(Gamer.this))
				.collect(Collectors.toList());
		// Now compare distance of this gamer versus the best one
		final Gamer firstEnemy = enemies.stream()
			.sorted()
			.findFirst()
			.get();
		final int difference = distance.getDistance()-firstEnemy.distance.getDistance();
		// if ahead, rush
		if(difference<0) {
			return findBestMove().decorateWith("RUSH !");
		} else if(wallsLeft>0) {
			// otherwise, trap at any location
			return trap(tested.getGamers(), tested).decorateWith(String.format("%d BEHIND.", difference));
		} else {
			return findBestMove().decorateWith("NO MORE WALLS LEFT !");
		}
	}

	private Action trap(final List<Gamer> gamers, final Playfield tested) {
		return tested.accept(new TrapGenerator(gamers, this));
	}

	private MoveTo findBestMove() {
		return Direction.DIRECTIONS.stream()
				.map((direction) ->
					direction.move(inPlayfield))
				.filter((direction) ->
					distanceMap.contains(direction))
				.sorted((first, second) ->
						distanceMap.getOrCreate(first).compareTo(distanceMap.getOrCreate(second)))
				.findFirst()
				.map((point) ->
						new MoveTo(point))
				.get();
	}

	/**
	 * Eager compute the distance map from gamer to the edge of the playfield it is aiming
	 * @param playfield
	 */
	private void computeDistanceToDestination(final Playfield playfield) {
		distanceMap =  new DistanceToDestinationComputer(this).computeOn(playfield);
		if(inPlayfield==null) {
			inPlayfield = playfield.toPlayfieldPositionForContent(x, y);
		}
		distance = distanceMap.get(inPlayfield);
	}

	@Override
	public int compareTo(final Gamer o) {
		return distance.compareTo(o.getDistance());
	}

	public DistanceInfo getDistance() {
		return distance;
	}

	public DistanceInfoPlayground getDistanceMap() {
		return distanceMap;
	}

	public DiscretePoint toPlayfieldPosition() {
		if(playfieldPostion==null) {
			playfieldPostion = new DiscretePoint(2*x, 2*y);
		}
		return playfieldPostion;
	}

}
