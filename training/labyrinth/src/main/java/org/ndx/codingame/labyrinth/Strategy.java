package org.ndx.codingame.labyrinth;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public abstract class Strategy {

	public Direction move(Agent agent, PlayField playground) {
		SortedSet<ScoredDirection<Integer>> scores = sortDirectionsByScore(agent, playground);
		return scores.first();
	}
	
	public Strategy mutate(DiscretePoint point) {
		return this;
	}

	protected SortedSet<ScoredDirection<Integer>> sortDirectionsByScore(Agent agent, PlayField playground) {
		SortedSet<ScoredDirection<Integer>> scores = new TreeSet<>(Comparator.comparing(ScoredDirection<Integer>::getScore).reversed().thenComparing(ScoredDirection::getX).thenComparing(ScoredDirection::getY));
		for (Direction d : Direction.DIRECTIONS) {
			ScoredDirection<Integer> scored = d.move(agent.position);
			scored.setScore(score(scored, playground));
			scores.add(scored);
		}
		System.err.println(toString()+" directions scores\n"+scores);
		return scores;
	}

	protected abstract int score(ScoredDirection scored, PlayField playground);

}
