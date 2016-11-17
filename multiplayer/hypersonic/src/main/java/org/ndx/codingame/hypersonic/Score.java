package org.ndx.codingame.hypersonic;

import java.util.ArrayList;
import java.util.Collection;

import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class Score implements Comparable<Score> {
	int innerScore;
	int length;
	int fullScore;
	ScoredDirection<Score> bestChild;
	Collection<ScoredDirection<Score>> children = new ArrayList<>();
	public Score(int innerScore) {
		super();
		this.innerScore = innerScore;
		this.length = 1;
	}
	public Score(int innerScore, int length) {
		super();
		this.innerScore = innerScore;
		this.length = length;
	}
	public boolean survive() {
		return length>=EvolvableConstants.HORIZON;
	}
	@Override
	public int compareTo(Score o) {
		int returned = (int) Math.signum(length-o.length);
		if(returned==0) {
			returned = (int) Math.signum(fullScore-o.fullScore); 
		}
		return returned;
	}
	public void addChild(ScoredDirection<Score> child) {
		children.add(child);
	}
	public void aggregate() {
		if(children.isEmpty()) {
			fullScore = innerScore;
		} else {
			int bestChildScore = Integer.MIN_VALUE;
			for (ScoredDirection<Score> scoredDirection : children) {
				Score score = scoredDirection.getScore();
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
