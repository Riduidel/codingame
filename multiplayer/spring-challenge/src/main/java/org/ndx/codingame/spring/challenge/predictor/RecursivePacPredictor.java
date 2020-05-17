package org.ndx.codingame.spring.challenge.predictor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.actions.MoveTo;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.actions.Speed;
import org.ndx.codingame.spring.challenge.actions.Switch;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.entities.VirtualPac;
import org.ndx.codingame.spring.challenge.playground.Cache;
import org.ndx.codingame.spring.challenge.playground.SpringPlayfield;

public class RecursivePacPredictor implements PacPredictor {

	private SpringPlayfield playfield;
	private Cache cache;
	AbstractPac my;
	/**
	 * List of pac predictions we're exploring
	 */
	private List<PacPredictor> growableChildren = new LinkedList<>();
	/**
	 * Action linked to this predictor
	 */
	PacAction action;
	/**
	 * List of possible actions we will evaluate here. It is built at construction
	 * time and consumed by the grow method
	 */
	private List<PacAction> possibleActions = new ArrayList<>();
	int deepness;
	private double localScore;
	private PacPredictor bestPrediction;
	private double bestScore = Double.NEGATIVE_INFINITY;
	private List<PacPredictor> terminatedChildren = new ArrayList<>();

	public RecursivePacPredictor(SpringPlayfield playfield, Cache cache, AbstractPac pac, DiscretePoint origin,
			int deepness, PacAction action) {
		this.playfield = playfield;
		this.cache = cache;
		this.my = pac;
		this.action = action;
		this.deepness = deepness + 1;
		this.localScore = computeScore() * (EvolvableConstants.HORIZON_FOR_RECURSIVE_PATH + 1 - deepness);
		if (deepness > EvolvableConstants.HORIZON_FOR_RECURSIVE_PATH) {
			if (cache.nearestPointsLoaded()) {
				growableChildren.add(new DistanceComputingPacPredictor(playfield, cache, pac, action, this.deepness));
			}
		} else {
			List<MoveTo> moves = createMoveActions(origin);
			possibleActions.addAll(moves);
			if(moves.isEmpty()) {
//				System.err.println("dead end");
			}
			if (deepness == 0) {
				if (pac.abilityCooldown == 0) {
					possibleActions.add(new Speed(pac));
					for (Type type : Type.values()) {
						if (type != Type.DEAD && type != pac.type) {
							possibleActions.add(new Switch(pac, type));
						}
					}
				}
			}
		}
		playfield.set(pac, pac);
	}

	private double computeScore() {
		double returned = 0;
		if(action!=null) {
			if(action instanceof MoveTo) {
				boolean firstPoint = true;
				for(DiscretePoint point : action.path()) {
					// Don't evaluate the first point !
					if(!firstPoint) {
						Content content = playfield.get(my);
						returned += content.accept(new ScoreComputer(my, action, deepness));
					}
					firstPoint = false;
				}
			} else if(action instanceof Switch) {
				returned = -1*EvolvableConstants.MAX_ABILITY_COOLDOWN;
			} else if(action instanceof Speed) {
				returned = -1*EvolvableConstants.MAX_SPEED_TURNS;
			}
		}
		return returned;
	}

	/**
	 * @param origin origin is here to exclude the direction we're coming from
	 * @return the list of valid moveto actions
	 */
	protected List<MoveTo> createMoveActions(DiscretePoint origin) {
		List<List<DiscretePoint>> nextPoints = getCache().getNextPointsCache(my);
		List<MoveTo> returned = new ArrayList<>();
		// Remove the list of points the origin is in, to avoid looping predictions
		for (List<DiscretePoint> navigable : nextPoints) {
			if (!navigable.contains(origin)) {
				// As we're building real move actions, let's evaluate only those which provide
				// real moves
				LinkedList<DiscretePoint> path = new LinkedList<>();
				for (DiscretePoint p : navigable) {
					if (playfield.get(p).canBeWalkedOnBy(my)) {
						path.add(p);
					}
				}
				if (!path.isEmpty() && !path.getLast().equals(my)) {
					returned.add(new MoveTo(my, path));
				}
			}
		}
		return returned;
	}

	public Cache getCache() {
		return cache;
	}

	/**
	 * Grow that pac predictor of one more pac prediction
	 * 
	 * @param iteration
	 */
	public void grow(int iteration) {
		PacPredictor evaluated = null;
		if (possibleActions.isEmpty()) {
			if (growableChildren.size() > 0) {
				evaluated = growableChildren.remove(0);
				evaluated.grow(iteration);
			}
		} else {
			PacAction action = possibleActions.remove(0);
			playfield.set(my, new VirtualPac(my.x, my.y, my.id, my.mine, my.type, my.speedTurnsLeft, my.abilityCooldown,
					playfield.get(my)));
			AbstractPac transformed = action.transform(playfield);
			// We make sure playfield still allow that move
			// to avoid locking of pacs
			if (playfield.get(transformed).canBeWalkedOnBy(transformed)) {
				evaluated = new RecursivePacPredictor(playfield, cache, transformed,
						// This one is really a hack: setting origin to null will allow non-move actions
						// to be handled properly
						action instanceof MoveTo ? new DiscretePoint(my.x, my.y) : null, deepness, action);
				growOrTerminate(evaluated);
			}
		}
		growOrTerminate(evaluated);
	}
	
	@Override
	public boolean isGrowable() {
		return !growableChildren.isEmpty() || !possibleActions.isEmpty();
	}

	private void growOrTerminate(PacPredictor evaluated) {
		if(bestPrediction==null) {
			bestPrediction=evaluated;
		} else if(evaluated!=null && evaluated.completeScore()>bestScore) {
			bestScore = evaluated.completeScore();
			bestPrediction = evaluated;
		}
		if(evaluated!=null) {
			if(evaluated.isGrowable()) {
				growableChildren.add(evaluated);
			} else {
				terminatedChildren.add(evaluated);
			}
		}
	}

	/**
	 * Only called at root level, so it is not a part of the interface
	 * 
	 * @return
	 */
	public PacAction getBestAction() {
		PacPredictor bestPrediction = getBestPrediction();
		PacAction returned = bestPrediction==null ? action : bestPrediction.getAction();
		return returned.withMessage("d="+depth()+";");
	}

	@Override
	public double completeScore() {
		PacPredictor bestPrediction = getBestPrediction();
		return localScore + (bestPrediction==null ? 0 :bestPrediction.completeScore());
	}

	@Override
	public int depth() {
		return getBestPrediction() == null ? deepness : getBestPrediction().depth();
	}

	private PacPredictor getBestPrediction() {
		return bestPrediction;
	}

	@Override
	public String toString() {
		return toString("\t").toString();
	}

	@Override
	public StringBuilder toString(String prefix) {
		StringBuilder sOut = new StringBuilder();
		sOut.append(prefix)
			.append(isGrowable() ? "GROWING" : "DONE").append(" ")
			.append("s=").append(completeScore()).append(";")
			.append("l=").append(localScore).append(";")
			.append("d=").append(deepness).append(";")
			.append(action).append("\n");
		if(bestPrediction!=null) {
			sOut.append(prefix).append("===BEST===\n");
			sOut.append(bestPrediction.toString(prefix+"\t"));
		}
		if(!growableChildren.isEmpty()) {
			sOut.append(prefix).append("===GROWABLE===\n");
			for (PacPredictor p : growableChildren) {
				sOut.append(p.toString(prefix + "\t"));
			}
		}
		if(!terminatedChildren.isEmpty()) {
			sOut.append(prefix).append("===TERMINATED===\n");
			for (PacPredictor p : terminatedChildren) {
				sOut.append(p.toString(prefix + "\t"));
			}
		}
		return sOut;
	}
	
	@Override public PacAction getAction() {
		return action;
	}
}
