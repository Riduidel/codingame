package org.ndx.codingame.labyrinth;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class Lookup extends Strategy {
	public Direction move(Agent agent, PlayField playground) {
		SortedSet<ScoredDirection<Integer>> scores = sortDirectionsByScore(agent, playground);
		ScoredDirection<Integer> best = scores.first();
		if(best.getScore()==0) {
			System.err.println("We're in a dead-end, applying fallback strategy");
			return agent.moveUsingFallback(playground);
		}
		return best;
	}

	@Override
	protected int score(ScoredDirection scored, PlayField playground) {
		return score(scored, playground, 0, 0, new HashSet<>());
	}

	public static int score(DiscretePoint point, PlayField playground, int base, int deepness, Set<DiscretePoint> knownPoints) {
		int returned = 0;
		int bonus = Agent.RANGE-deepness;
		bonus = bonus*bonus;
		if(deepness<Agent.RANGE) {
			if(!knownPoints.contains(point)) {
				if(playground.contains(point)) {
					knownPoints.add(point);
					try {
						char content = playground.get(point);
						switch(content) {
						case '#':
							return returned;
						case '?':
							return 2*bonus;
						case 'C':
							// Never move onto control point
							return -20*bonus;
						}
						for (Direction d : Direction.DIRECTIONS) {
							DiscretePoint moved = d.move(point);
							returned+=score(moved, playground, base, deepness+1, knownPoints);
						}
					} finally {
						knownPoints.remove(point);
					}
				}
			}
		}
		return returned;
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}
