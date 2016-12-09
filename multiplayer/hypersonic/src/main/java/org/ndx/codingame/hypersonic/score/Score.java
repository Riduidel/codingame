package org.ndx.codingame.hypersonic.score;

import java.util.ArrayList;
import java.util.Collection;

import org.ndx.codingame.hypersonic.EvolvableConstants;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class Score implements Comparable<Score> {
	private final int innerScore;
	private int length;
	private int fullScore;
	public ScoredDirection<Score> bestChild;
	Collection<ScoredDirection<Score>> children = new ArrayList<>();
	public Score(final int innerScore) {
		super();
		this.innerScore = innerScore;
		length = 1;
	}
	public Score(final int innerScore, final int length) {
		super();
		this.innerScore = innerScore;
		this.length = length;
	}
	public boolean survive() {
		return length>=EvolvableConstants.HORIZON;
	}
	@Override
	public int compareTo(final Score o) {
		int returned = (int) Math.signum(length-o.length);
		if(returned==0) {
			returned = (int) Math.signum(fullScore-o.fullScore); 
		}
		return returned;
	}
	public void addChild(final ScoredDirection<Score> child) {
		children.add(child);
	}
	public void aggregate() {
		if(children.isEmpty()) {
			fullScore = innerScore;
		} else {
			int bestChildScore = Integer.MIN_VALUE;
			for (final ScoredDirection<Score> scoredDirection : children) {
				final Score score = scoredDirection.getScore();
				if(score.length>=length) {
					if(score.fullScore>bestChildScore) {
						bestChildScore = score.fullScore;
						bestChild = scoredDirection;
						length = bestChild.getScore().length;
					}
				}
			}
			fullScore = innerScore+bestChildScore;
		}
	}
	@Override
	public String toString() {
		return String.format("(inner=%s, length=%s, full=%s, best=%s)", innerScore, length, fullScore, bestChild);
	}
}
