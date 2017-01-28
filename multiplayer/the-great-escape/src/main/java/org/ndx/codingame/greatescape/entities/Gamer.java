package org.ndx.codingame.greatescape.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.greatescape.actions.Action;
import org.ndx.codingame.greatescape.actions.MoveTo;
import org.ndx.codingame.greatescape.actions.Trap;
import org.ndx.codingame.greatescape.playground.DistanceInfo;
import org.ndx.codingame.greatescape.playground.DistanceToDestinationComputer;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class Gamer extends DiscretePoint implements GameElement {

	private final int wallsLeft;
	public final Direction direction;
	private DistanceInfo distanceMap;
	private DiscretePoint inPlayfield;
	private int distance;

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
		final List<Gamer> enemies = tested.getGamers().stream()
				.filter((g)-> !g.equals(Gamer.this))
				.collect(Collectors.toList());
		// First step, obtain for each gamer the distance
		tested.getGamers().stream().forEach((g) -> g.computeDistanceToDestination(tested));
		// Now compare distance of this gamer versus the other
		final Gamer firstEnemy = enemies.stream()
			.sorted((a, b) -> a.distance - b.distance)
			.findFirst()
			.get();
		final int difference = firstEnemy.distance-distance;
		if(difference<0) {
			System.err.println("RUSH TO THE END !");
			return findBestMove();
		} else {
			System.err.println(String.format("Trapping enemy. We're %d behind", difference));
			return firstEnemy.trap(this, tested);
		}
	}

	private Action trap(final Gamer me, final Playfield tested) {
		Orientation orientation;
		if(direction.equals(Direction.LEFT) || direction.equals(Direction.RIGHT)) {
			orientation = Orientation.V;
		} else {
			orientation = Orientation.H;
		}
		final ScoredDirection<Object> move = direction.move(this);
		int x = move.x;
		int y = move.y;
		if(move.x>=tested.visibleWidth-1) {
			x--;
		}
		if(move.y>=tested.visibleHeight-1) {
			y--;
		}
		final Trap possible = new Trap(orientation, new DiscretePoint(x, y));
		if(possible.canInstall(tested)) {
			return possible;
		} else {
			return me.findBestMove();
		}
	}

	private MoveTo findBestMove() {
		return Direction.DIRECTIONS.stream()
				.map((direction) ->
					direction.move(inPlayfield))
				.filter((direction) ->
					distanceMap.contains(direction))
				.sorted((first, second) ->
						distanceMap.get(first)-distanceMap.get(second))
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

}
