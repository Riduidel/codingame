package org.ndx.codingame.spring.challenge.predictor;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.PotentialSmallPill;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.playground.Cache;
import org.ndx.codingame.spring.challenge.playground.SpringPlayfield;

public class DistanceComputingPacPredictor implements PacPredictor {
	private SpringPlayfield playfield;
	private Cache cache;
	private AbstractPac my;
	private int deepness;
	private double localScore;

	private ImmutablePlayground<Double> buildDistancesScoresForPacs(AbstractPac my) {
		ImmutablePlayground<Double> scores = playfield.getZero();
		for (AbstractPac pac : playfield.getAllPacs()) {
			if(pac.type==Type.DEAD) {
			} else if(pac.mine) {
				scores = scores.apply(cache.usingDistanceTo(pac),
						(a, b) -> a +
						(b<EvolvableConstants.DISTANCE_TEAMMATE_TOO_CLOSE ?
								EvolvableConstants.INTERNAL_SCORE_FOR_TEAMMATE_TOO_CLOSE : 0
								) / (1 + b));
			} else {
				if(pac.isDangerousFor(my)) {
					scores = scores.apply(cache.usingDistanceTo(pac),
							(a, b) -> a +
							(b<EvolvableConstants.DISTANCE_ENEMY_TOO_CLOSE ?
									EvolvableConstants.INTERNAL_SCORE_FOR_ENEMY_PREDATOR : 0
									) / (1 + b));
				} else  {
					scores = scores.apply(cache.usingDistanceTo(pac),
							(a, b) -> a +
							(b<EvolvableConstants.DISTANCE_ENEMY_TOO_CLOSE ?
									EvolvableConstants.INTERNAL_SCORE_FOR_ENEMY_PREY : 0
									) / (1 + b));
				}
			}
		}
		return scores;
	}

	private ImmutablePlayground<Double> buildDistancesScoresForPills(AbstractPac my) {
		ImmutablePlayground<Double> scores = playfield.getZero();
		for (BigPill big : playfield.getBigPills()) {
			scores = scores.apply(cache.usingDistanceTo(big),
					(a, b) -> a + EvolvableConstants.INTERNAL_SCORE_FOR_BIG_PILL / (1 + b));
		}
		for (SmallPill small : playfield.getSmallPills()) {
			scores = scores.apply(cache.usingDistanceTo(small),
					(a, b) -> a + EvolvableConstants.INTERNAL_SCORE_FOR_SMALL_PILL / (1 + b));
		}
		if(scores==playfield.getZero()) {
			// Now add the n nearest potential pills
			DiscretePoint preceding = null;
			for (DiscretePoint point : cache.getPointsSortedByDistanceTo(my)) {
				if (PotentialSmallPill.instance == playfield.get(point)) {
					if(preceding!=null) {
						// We want to target the nearest cluster of points
						if(preceding.distance1To(point)>1) {
							break;
						}
					}
					scores = scores.apply(cache.usingDistanceTo(point),
							(a, b) -> a + EvolvableConstants.INTERNAL_SCORE_FOR_POTENTIAL_SMALL_PILL / (1 + b));
				}
			}
		}
		return scores;
	}


	public DistanceComputingPacPredictor(SpringPlayfield playfield, Cache cache, AbstractPac pac,
			int deepness) {
		this.playfield = playfield;
		this.cache = cache;
		this.my = pac;
		this.deepness = deepness+1;
		this.localScore = computeLocalScore()/(this.deepness*this.deepness);
	}

	private double computeLocalScore() {
		ImmutablePlayground<Double> scores = buildDistancesScoresForPills(my);
		scores = scores.apply(buildDistancesScoresForPacs(my),
				(a, b) -> a+b);
		return scores.get(my);
	}

	/**
	 * This pac computer can't grow, because it simply computes distances
	 */
	@Override
	public boolean grow(int iteration) {
		return false;
	}

	@Override
	public double completeScore() {
		return localScore;
	}

	@Override
	public StringBuilder toString(String prefix) {
		return new StringBuilder(prefix)
				.append("DISTANCES").append("s=").append(completeScore()).append("\n");
	}

	@Override
	public int depth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
