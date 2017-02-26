package org.ndx.codingame.greatescape.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.ndx.codingame.greatescape.actions.Action;
import org.ndx.codingame.greatescape.actions.MoveTo;
import org.ndx.codingame.greatescape.computers.DistancePlayground;
import org.ndx.codingame.greatescape.computers.DistancePlaygroundBuilder;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class Gamer extends DiscretePoint implements GameElement {
	private final int wallsLeft;
	public final Direction direction;
	private DiscretePoint inPlayfield;
	private DiscretePoint playfieldPostion;
	private DistancePlayground distancePlayground;
	private MoveTo nextMove;

	public Gamer(final int x, final int y, final int wallsLeft, final Direction direction) {
		super(x, y);
		this.wallsLeft = wallsLeft;
		this.direction = direction;
	}

	public Gamer(final ScoredDirection<Object> move, final int wallsLeft, final Direction direction) {
		this(move.x, move.y, wallsLeft, direction);
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
		List<Gamer> gamers = tested.getGamers();
		gamers.stream().forEach((g) -> g.computeDistanceToDestination(tested));
		gamers = anticipate(tested, gamers);
		final List<Gamer> enemies = gamers.stream().filter((g) -> !g.equals(Gamer.this)).collect(Collectors.toList());
		// Now compare distance of this gamer versus the best one
		final Gamer firstEnemy = enemies.stream().sorted().findFirst().get();
		return nextMove;
	}

	private List<Gamer> anticipate(final Playfield tested, final List<Gamer> gamers) {
		final List<Gamer> returned = new ArrayList<>();
		boolean stopAnticipate = false;
		for (Gamer gamer : gamers) {
			if (this == gamer) {
				stopAnticipate = true;
			} else {
				if (!stopAnticipate) {
					tested.set(gamer, NOTHING);
					gamer = new Gamer(nextMove.direction.move(gamer), wallsLeft, direction);
					tested.set(gamer, gamer);
				}
			}
			returned.add(gamer);
		}
		return returned;
	}

	private void computeDistanceToDestination(final Playfield tested) {
		distancePlayground = new DistancePlaygroundBuilder(this).computeOn(tested);
		// Now we have a playground, immediatly compute theoretical next move
		final Map<Integer, List<DiscretePoint>> scores = Direction.DIRECTIONS.stream()
				.filter(direction -> distancePlayground.contains(direction))
				.collect(Collectors.groupingBy(direction -> distancePlayground.get(direction).getReverseDistance()));
		final Integer distance = new TreeSet<>(scores.keySet()).first();
		final DiscretePoint next = scores.get(distance).get(0);
		nextMove = buildNextMoveTo(next);
	}

	private MoveTo buildNextMoveTo(final DiscretePoint next) {
		return new MoveTo(Direction.between(this, next));
	}

	public DiscretePoint toPlayfieldPosition() {
		if (playfieldPostion == null) {
			playfieldPostion = new DiscretePoint(2 * x, 2 * y);
		}
		return playfieldPostion;
	}

	public int getDistance() {
		return distancePlayground.getDistance(toPlayfieldPosition());
	}
}
