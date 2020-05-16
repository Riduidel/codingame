package org.ndx.codingame.spring.challenge.predictor;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.actions.PacAction;
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
	private PacAction notThisAction;

	public DistanceComputingPacPredictor(SpringPlayfield playfield, Cache cache, AbstractPac pac,
			PacAction action, int deepness) {
		this.playfield = playfield;
		this.cache = cache;
		this.my = pac;
		this.notThisAction = action;
		// Notice that deeepness is already taken in account as a distance
		this.localScore = computeLocalScore();
	}

	private double computeLocalScore() {
		double score = 0;
		ScoreComputer scoreComputer = new ScoreComputer(my, notThisAction, deepness);
		ImmutablePlayground<Double> distances = cache.usingDistanceTo(my);
		for(DiscretePoint point : cache.getPointsSortedByDistanceTo(my)) {
			if(!point.equals(my)) {
				score = score + playfield.get(point).accept(scoreComputer)/distances.get(point);
			}
		}
		return score;
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
		return deepness;
	}

	@Override
	public PacAction getAction() {
		return notThisAction;
	}
}
