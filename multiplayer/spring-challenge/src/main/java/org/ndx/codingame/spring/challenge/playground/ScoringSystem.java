package org.ndx.codingame.spring.challenge.playground;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.Content;

/**
 * This produces array of scores allowing us to move towards big pills
 *  and away from (enemy) pacs
 * @author nicolas-delsaux
 *
 */
public class ScoringSystem {

	public final ImmutablePlayground<Integer> distances;
	private Content targetContent;
	public final ImmutablePlayground<Integer> scores;

	public ScoringSystem(Content c, ImmutablePlayground<Integer> computeDistancesTo) {
		this.targetContent = c;
		this.distances = computeDistancesTo;
		this.scores = distances.apply(distance -> EvolvableConstants.INTERNAL_SCORE_FOR_BIG_PILL-distance*distance); 
	}

}
