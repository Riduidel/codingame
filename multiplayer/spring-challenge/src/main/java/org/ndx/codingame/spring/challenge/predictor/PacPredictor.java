package org.ndx.codingame.spring.challenge.predictor;

import java.util.Comparator;

import org.ndx.codingame.spring.challenge.actions.PacAction;

public interface PacPredictor {

	public class ByScoreAndDepth implements Comparator<PacPredictor> {

		@Override
		public int compare(PacPredictor o1, PacPredictor o2) {
			int returned = Double.compare(o1.completeScore(), o2.completeScore());
			if(returned==0) {
				returned = Integer.compare(o1.depth(), o2.depth());
			}
			return returned;
		}

	}

	/**
	 * Grow the prediction tree by one level.
	 * @param iteration iteration index. Can be used as a message
	 * @return true if prediction tree can continue, false otherwise
	 */
	boolean grow(int iteration);

	/**
	 * Get complete score for this prediction
	 * @return sum of the best score until the end leaf of this prediction
	 */
	double completeScore();

	public StringBuilder toString(String prefix);

	/**
	 * @return Depth of this predictor
	 */
	int depth();

	PacAction getAction();
}
