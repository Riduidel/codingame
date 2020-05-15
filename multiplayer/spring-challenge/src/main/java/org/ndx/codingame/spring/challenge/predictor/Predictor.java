package org.ndx.codingame.spring.challenge.predictor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ndx.codingame.lib2d.MutablePlayground;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.playground.Cache;
import org.ndx.codingame.spring.challenge.playground.SpringPlayfield;

public class Predictor {

	private SpringPlayfield playfield;
	private Cache cache;
	private Map<Pac, RecursivePacPredictor> predictors = new HashMap<>();
	private SpringPlayfield virtualPlayfield;

	public Predictor(SpringPlayfield playfield) {
		this.playfield = playfield;
		this.cache = playfield.getCache();
		this.virtualPlayfield = playfield.readWriteProxy();
		// Immediatly map alive pacs to their specific predictors
		for(Pac pac : playfield.getMyPacs()) {
			predictors.put(pac, 
					new RecursivePacPredictor(
						virtualPlayfield,
						cache,
						pac,
						pac,
						0,
						null));
		}
	}

	public boolean grow(int iteration) {
		boolean returned = false;
		for(PacPredictor predictor : predictors.values()) {
			// put returned after grow call to avoid short-circuit
			returned = predictor.grow(iteration) || returned;
		}
		return returned;
	}

	public Map<Pac, PacAction> getBestPredictions(String message) {
		Map<Pac, PacAction> returned = new HashMap<>();
		for(Map.Entry<Pac, RecursivePacPredictor> predictor : predictors.entrySet()) {
			returned.put(predictor.getKey(), predictor.getValue().getBestAction().withMessage(message));
		}
		return returned;
	}

	@Override
	public String toString() {
		return "Predictor [predictors=" + predictors + "]";
	}

}
