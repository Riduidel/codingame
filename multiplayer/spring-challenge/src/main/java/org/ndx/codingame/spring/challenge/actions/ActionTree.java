package org.ndx.codingame.spring.challenge.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.playground.SpringChallengePlayground;
import org.ndx.codingame.spring.challenge.playground.Turn;

public class ActionTree {
	
	private SpringChallengePlayground source;
	private Turn turn;
	private Pac pac;
	
	private Map<PacAction, ActionTree> descendants = new HashMap<>();

	/** List of possible next points. Will be shrinked by the {@link #grow()} method */
	private List<DiscretePoint> nextPoints;

	private List<PacAction> nextMoves;
	
	private PacAction bestDescendant;

	private final int deepness;
	
	private int internalScore;
	
	private int loop;
	private boolean deadEnd;

	public ActionTree(SpringChallengePlayground source, Turn turn, Pac pac, DiscretePoint trail, int deepness) {
		this.source = source;
		this.turn = turn;
		this.pac = pac;
		this.deepness = deepness;
		// Create a copy to filter it later
		this.nextPoints = new ArrayList<>(source.nextPointsAt(pac));
		this.nextPoints.remove(trail);
		deadEnd = nextPoints.isEmpty();
		this.internalScore = computeScore();
		turn.set(pac, pac);
	}

	private int computeScore() {
		Content valueAtTurn = turn.get(pac);
		// Just to avoid being bothered by status at startup
		int directScore = 0;
		if(valueAtTurn instanceof Pac) {
			Pac other = (Pac) valueAtTurn;
			// KEEP THIS LINE! Because otherwise pac will go in deadends
			if(!pac.equals(valueAtTurn)) {
				directScore = valueAtTurn.score();
			}
		} else {
			directScore = valueAtTurn.score();
		}
//		directScore+=turn.getBigPillScore(pac).get(pac);
		int deepnessAdjuster = Math.max(EvolvableConstants.HORIZON_FOR_RANDOM_PATH-deepness, 1);
		int cumulatedScore = directScore*deepnessAdjuster;
		if(nextPoints.isEmpty()) {
			// Put a malus on dead end
			cumulatedScore+=EvolvableConstants.INTERNAL_SCORE_FOR_DEAD_END;
		}
		return cumulatedScore;
	}

	public int score() {
		return internalScore + (descendants.isEmpty() ? 0 : descendants.get(bestDescendant).score());
	}

	/**
	 * Grow the tree of one fold
	 */
	public boolean grow() {
		PacAction candidate = null;
		if(nextPoints.isEmpty()) {
			if(nextMoves!=null) {
				if(!deadEnd) {
					// next moves will be empty if we're in a dead end
					candidate = nextMoves.get(loop++%nextMoves.size());
					deadEnd = descendants.get(candidate).grow();
				}
			}
		} else if(nextPoints.size()==1) {
			candidate = generateAction(nextPoints.remove(0));
			nextMoves = new ArrayList<>(descendants.keySet());
		} else {
			candidate = generateAction(nextPoints.remove(0));
		}
		if(bestDescendant==null) {
			bestDescendant = candidate;
		} else if(candidate!=null){
			if(descendants.get(candidate).score()>descendants.get(bestDescendant).score()) {
				bestDescendant = candidate;
			}
		}
		return deadEnd;
	}

	private PacAction generateAction(DiscretePoint remove) {
		PacAction returned = new MoveTo(pac, remove);
		descendants.put(returned, createActionTreeFor(returned));
		return returned;
	}

	private ActionTree createActionTreeFor(PacAction returned) {
		Turn nextTurn = turn.readWriteProxy();
		nextTurn.set(pac, Ground.instance);
		Pac nextPac = returned.transform();
		return new ActionTree(source, nextTurn, nextPac, new DiscretePoint(pac), deepness+1);
	}

	public PacAction getFirstAction() {
		return bestDescendant.withMessage(loop+"");
	}
	
	@Override
	public String toString() {
		return toString("").toString();
	}

	private StringBuilder toString(String prefix) {
		StringBuilder returned = new StringBuilder();
		returned.append("ActionTree [score=").append(score()).append(", pac=").append(pac).append("]\n");
		for(Map.Entry<PacAction, ActionTree> entry : descendants.entrySet()) {
			returned.append(prefix).append(entry.getValue().toString("\t"+prefix));
		}
		return returned;
	}
}
