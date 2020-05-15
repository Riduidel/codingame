package org.ndx.codingame.spring.challenge.predictor;

import org.ndx.codingame.spring.challenge.actions.PacAction;

public interface PacPredictor {

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

	int depth();
}
