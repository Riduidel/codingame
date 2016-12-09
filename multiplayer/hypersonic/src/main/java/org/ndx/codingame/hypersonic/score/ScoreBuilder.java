package org.ndx.codingame.hypersonic.score;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ndx.codingame.hypersonic.EvolvableConstants;
import org.ndx.codingame.hypersonic.entities.Bomb;
import org.ndx.codingame.hypersonic.entities.Box;
import org.ndx.codingame.hypersonic.entities.ContentVisitor;
import org.ndx.codingame.hypersonic.entities.Fire;
import org.ndx.codingame.hypersonic.entities.FireThenItem;
import org.ndx.codingame.hypersonic.entities.Gamer;
import org.ndx.codingame.hypersonic.entities.Item;
import org.ndx.codingame.hypersonic.entities.Nothing;
import org.ndx.codingame.hypersonic.entities.Wall;
import org.ndx.codingame.hypersonic.playground.OpportunitesLoader;
import org.ndx.codingame.hypersonic.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class ScoreBuilder {
	private class ScoreBuilderVisitor implements ContentVisitor<ScoredDirection<Score>>	 {
		private ScoredDirection<Score> buildScore(final int value, final boolean stopHere) {
			ScoredDirection<Score> returned;
			if(point instanceof ScoredDirection) {
				returned = (ScoredDirection<Score>) point;
			} else {
				returned = new ScoredDirection<>(point.x, point.y, ""); 
			}
			returned.setScore(new Score(value*factor, iteration));
			if(!stopHere) {
				if(next!=null) {
					// Now add children scores
					for (final Direction direction : directions(point)) {
						final ScoredDirection<Score> move = direction.move(point);
						if(next.source.allow(move)) {
							returned.getScore().addChild(next.compute(move));
						}
					}
				}
			}
			returned.getScore().aggregate();
			return returned;
		}

		@Override
		public ScoredDirection<Score> visitNothing(final Nothing nothing) {
			return buildScore(opportunities.get(point), false);
		}

		@Override
		public ScoredDirection<Score> visitBox(final Box box) {
			return buildScore(EvolvableConstants.SCORE_VISIT_BOX, true);
		}

		@Override
		public ScoredDirection<Score> visitWall(final Wall wall) {
			return buildScore(EvolvableConstants.SCORE_VISIT_WALL, true);
		}

		@Override
		public ScoredDirection<Score> visitGamer(final Gamer gamer) {
			return buildScore(EvolvableConstants.SCORE_VISIT_GAMER, !firstTurn);
		}

		@Override
		public ScoredDirection<Score> visitBomb(final Bomb bomb) {
			return buildScore(EvolvableConstants.SCORE_VISIT_BOMB, !firstTurn);
		}

		@Override
		public ScoredDirection<Score> visitItem(final Item item) {
			if(countVisits()>1) {
				return buildScore(EvolvableConstants.SCORE_NOTHING, false);
			} else {
				return buildScore(EvolvableConstants.SCORE_CATCHED_ITEM, false);
			}
		}

		@Override
		public ScoredDirection<Score> visitFire(final Fire fire) {
			return buildScore(EvolvableConstants.SCORE_SUICIDE, true);
		}

		@Override
		public ScoredDirection<Score> visitFireThenItem(final FireThenItem fireThenItem) {
			return buildScore(EvolvableConstants.SCORE_SUICIDE, true);
		}
		
	}
	private Playground<List<Direction>> directions;
	int iteration;
	int factor;
	Playfield source;
	private final ScoreBuilderVisitor visitor;
	boolean firstTurn;
	/**
	 * Map linking the positions to the number of time they have been visited, shared between all instances
	 */
	Map<DiscretePoint, Integer> visited;
	ScoreBuilder next;
	Playground<ScoredDirection<Score>> cache;
	DiscretePoint point;
	Playground<Integer> opportunities;

	public ScoreBuilder(final Playfield playground, final OpportunitesLoader opportunitiesLoader) {
		this(playground, opportunitiesLoader, 0);
	}

	public ScoreBuilder(final Playfield playground, final OpportunitesLoader opportunitiesLoader, final int i) {
		visitor = new ScoreBuilderVisitor();
		cache = new Playground<>(playground.width, playground.height);
		iteration = i;
		factor = EvolvableConstants.HORIZON-iteration;
		source = playground;
		opportunities = opportunitiesLoader.findOpportunities(source);
		if(i<=EvolvableConstants.HORIZON) {
			next = new ScoreBuilder(playground.next(), opportunitiesLoader, i+1);
			directions = next.directions;
			visited = next.visited;
		} else {
			directions = new Playground<>(playground.width, playground.height);
			visited = new LinkedHashMap<>();
		}
	}

	public ScoredDirection<Score> compute(final ScoredDirection<Score> move) {
		if(source.contains(move)) {
			ScoredDirection<Score> returned = cache.get(move);
			addInVisited(move);
			if(returned==null) {
				point = move;
				returned = source.get(move).accept(visitor);
				cache.set(move, returned);
			}
			removeFromVisited(move);
			return returned;
		} else {
			return move.withScore(new Score(EvolvableConstants.SCORE_OUTSIDE));
		}
	}

	public ScoredDirection<Score> computeFor(final DiscretePoint point) {
		this.point = point;
		firstTurn = true;
		addInVisited(point);
		final ScoredDirection<Score> computed = source.get(point).accept(visitor);
		removeFromVisited(point);
		if(!computed.getScore().survive()) {
			return computed;
		} else {
			return computed.getScore().bestChild;
		}
	}

	private void removeFromVisited(final DiscretePoint point) {
		visited.put(point, visited.get(point)-1);
	}

	private void addInVisited(final DiscretePoint point) {
		if(!visited.containsKey(point)){
			visited.put(point, 0);
		}
		visited.put(point, visited.get(point)+1);
	}

	private int countVisits() {
		return visited.get(point);
	}

	List<Direction> directions(final DiscretePoint point) {
		List<Direction> returned = directions.get(point);
		if(returned==null) {
			returned = new ArrayList<>();
			if(point.x<directions.width/2) {
				returned.add(Direction.RIGHT);
				returned.add(Direction.LEFT);
			} else {
				returned.add(Direction.LEFT);
				returned.add(Direction.RIGHT);
			}
			if(point.y<directions.height/2) {
				returned.add(Direction.DOWN);
				returned.add(Direction.UP);
			} else {
				returned.add(Direction.UP);
				returned.add(Direction.DOWN);
			}
			returned.add(Direction.STAY);
			directions.set(point, returned);
		}
		return returned;
	}
}
