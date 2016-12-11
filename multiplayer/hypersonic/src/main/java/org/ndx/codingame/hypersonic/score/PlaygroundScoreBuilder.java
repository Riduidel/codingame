package org.ndx.codingame.hypersonic.score;

import org.ndx.codingame.hypersonic.EvolvableConstants;
import org.ndx.codingame.hypersonic.entities.Bomb;
import org.ndx.codingame.hypersonic.entities.Box;
import org.ndx.codingame.hypersonic.entities.ContentVisitor;
import org.ndx.codingame.hypersonic.entities.Fire;
import org.ndx.codingame.hypersonic.entities.FireThenItem;
import org.ndx.codingame.hypersonic.entities.Gamer;
import org.ndx.codingame.hypersonic.entities.Item;
import org.ndx.codingame.hypersonic.entities.Nothing;
import org.ndx.codingame.hypersonic.entities.VirtualBomb;
import org.ndx.codingame.hypersonic.entities.Wall;
import org.ndx.codingame.hypersonic.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class PlaygroundScoreBuilder {
	private class ScoreBuilderVisitor implements ContentVisitor<ScoredDirection<Score>>	 {
		private final DiscretePoint point;

		public ScoreBuilderVisitor(final DiscretePoint point) {
			this.point = point;
		}
		private ScoredDirection<Score> buildScore(final int value, final boolean stopHere) {
			ScoredDirection<Score> returned;
			if(point instanceof ScoredDirection) {
				returned = (ScoredDirection<Score>) point;
			} else {
				returned = new ScoredDirection<>(point.x, point.y, ""); 
			}
			returned.setScore(new Score(value, iteration));
			if(!stopHere) {
				if(!reachedHorizon) {
					// Now add children scores
					for (final Direction direction : parent.getDirectionsFor(point)) {
						final ScoredDirection<Score> move = direction.move(point);
						// only possible since playground and next value both have same characteristics
						if(playground.allow(move)) {
							returned.getScore().addChild(computeNextFor(move));
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
			return buildScore(EvolvableConstants.SCORE_VISIT_BOMB, true);
		}

		@Override
		public ScoredDirection<Score> visitVirtualBomb(final VirtualBomb bomb) {
			return buildScore(EvolvableConstants.SCORE_VISIT_BOMB, !firstTurn);
		}

		@Override
		public ScoredDirection<Score> visitItem(final Item item) {
			return buildScore(EvolvableConstants.SCORE_CATCHED_ITEM, false);
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

	public final ScoreBuilder parent;
	private final Playfield playground;
	public final int iteration;
	private final Playground<ScoredDirection<Score>> cache;
	public final int factor;
	public final Playground<Integer> opportunities;
	public final boolean firstTurn;
	public final boolean reachedHorizon;

	public PlaygroundScoreBuilder(final ScoreBuilder scoreBuilder, final Playfield playground, final int index) {
		parent = scoreBuilder;
		this.playground = playground;
		iteration = index;
		factor = EvolvableConstants.HORIZON-iteration;
		firstTurn = index==0;
		reachedHorizon = index==EvolvableConstants.HORIZON;
		cache = new Playground<>(playground.width, playground.height);
		opportunities = parent.computeOpportunitiesFor(playground);
	}

	public ScoredDirection<Score> computeNextFor(final ScoredDirection<Score> move) {
		return parent.computeNextFor(playground.next(), move, iteration+1);
	}

	public ScoredDirection<Score> computeFor(final DiscretePoint point) {
		if(!cache.has(point)) {
			cache.set(point, playground.get(point).accept(new ScoreBuilderVisitor(point)));
		}
		return cache.get(point);
	}

}
