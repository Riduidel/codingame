package org.ndx.codingame.spring.challenge.predictor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ndx.codingame.lib2d.MutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.actions.MoveTo;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.actions.Speed;
import org.ndx.codingame.spring.challenge.actions.Switch;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.ContentAdapter;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.PacTrace;
import org.ndx.codingame.spring.challenge.entities.PotentialSmallPill;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.entities.VirtualPac;
import org.ndx.codingame.spring.challenge.playground.Cache;
import org.ndx.codingame.spring.challenge.playground.SpringPlayfield;

public class RecursivePacPredictor implements PacPredictor {

	private final class ScoreComputer extends ContentAdapter<Double> {
		private ScoreComputer(Double returned) {
			super(returned);
		}

		private double simpleScore(Content content) {
			return content.score();
		}

		@Override
		public Double visitSmallPill(SmallPill smallPill) {
			action.withMessage("on pill;");
			return simpleScore(smallPill);
		}

		@Override
		public Double visitBigPill(BigPill bigPill) {
			action.withMessage("on PILL;");
			return simpleScore(bigPill);
		}

		@Override
		public Double visitPotentialSmallPill(PotentialSmallPill potentialSmallPill) {
			action.withMessage("on potential pill;");
			return simpleScore(potentialSmallPill);
		}

		@Override
		public Double visitPac(Pac pac) {
			if (pac.type != Type.DEAD) {
				if (pac.mine) {
					// On first predictor run, action is null
					if (action != null)
						action.withMessage("on my pac;");
					if (pac.id == my.id) {
						return 0.0;
					} else {
						return (double) EvolvableConstants.INTERNAL_SCORE_FOR_TEAMMATE_TOO_CLOSE;
					}
				} else {
					if (pac.isDangerousFor(my)) {
						if (deepness < EvolvableConstants.DISTANCE_ENEMY_TOO_CLOSE) {
							action.withMessage("on predator;");
							return (double) EvolvableConstants.INTERNAL_SCORE_FOR_ENEMY_PREDATOR;
						}
					} else if (my.isDangerousFor(pac)) {
						// Take care and only attack enemies which can't mutate
						action.withMessage("on prey;");
						if(pac.speedTurnsLeft>0) {
							action.withMessage("prey too fast;");
						} else if (pac.abilityCooldown < deepness) {
							action.withMessage("unsure;");
						} else {
							action.withMessage("sure;");
							return (double) EvolvableConstants.INTERNAL_SCORE_FOR_ENEMY_PREY;
						}
					}
				}
			}
			return 0.0;
		}

		@Override
		public Double visitVirtualPac(VirtualPac virtualPac) {
			return virtualPac.original.accept(this);
		}

		@Override
		public Double visitPacTrace(PacTrace pacTrace) {
			if (pacTrace.mine) {
				action.withMessage("on my pac trac");
			} else {
				if (pacTrace.isDangerousFor(my)) {
					action.withMessage("on predator pac trace");
					return -1.0 * EvolvableConstants.INTERNAL_SCORE_FOR_PAC_TRACE;
				} else {
					action.withMessage("on prey pac trace");
					return (double) EvolvableConstants.INTERNAL_SCORE_FOR_PAC_TRACE;
				}
			}
			return 0.0;
		}
	}

	private SpringPlayfield playfield;
	private Cache cache;
	private AbstractPac my;
	/**
	 * List of pac predictions we're exploring
	 */
	private List<PacPredictor> children = new ArrayList<>();
	/**
	 * Action linked to this predictor
	 */
	private PacAction action;
	/**
	 * List of possible actions we will evaluate here. It is built at construction
	 * time and consumed by the grow method
	 */
	private List<PacAction> possibleActions = new ArrayList<>();
	private int deepness;
	private double localScore;
	private int iterator = 0;

	public RecursivePacPredictor(SpringPlayfield playfield, Cache cache, AbstractPac pac, DiscretePoint origin,
			int deepness, PacAction action) {
		this.playfield = playfield;
		this.cache = cache;
		this.my = pac;
		this.action = action;
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
		this.deepness = deepness + 1;
		this.localScore = computeScore() / (this.deepness * this.deepness);
		List<MoveTo> moves = createMoveActions(origin);
		if (deepness > EvolvableConstants.HORIZON_FOR_RANDOM_PATH) {
			if (cache.nearestPointsLoaded()) {
				children.add(new DistanceComputingPacPredictor(playfield, cache, pac, this.deepness));
			}
		} else {
			possibleActions.addAll(moves);
		}
		playfield.set(pac, pac);
	}

	private double computeScore() {
		double returned = 0;
		if(action!=null) {
			for(DiscretePoint point : action.path()) {
				Content content = playfield.get(my);
				returned += content.accept(new ScoreComputer(0.0));
			}
		}
		return returned;
	}

	/**
	 * This method emits true when
	 * 
	 * @param origin origin is here to exclude the direction we're coming from
	 * @return
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
	 * @return
	 */
	public boolean grow(int iteration) {
		boolean continueToGrow = false;
		PacPredictor evaluated = null;
		if (possibleActions.isEmpty()) {
			if (children.size() > 0) {
				evaluated = children.remove(iterator++ % children.size());
				continueToGrow = evaluated.grow(iteration);
			}
		} else {
			PacAction action = possibleActions.remove(iterator % possibleActions.size());
			action.withMessage("i=" + iteration + ";");
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
				children.add(evaluated);
			}
			continueToGrow = true;
		}
		if (evaluated != null) {
			children.add(evaluated);
		}
		children.sort(new PacPredictor.ByScoreAndDepth().reversed());
		return continueToGrow;
	}

	/**
	 * Only called at root level, so it is not a part of the interface
	 * 
	 * @return
	 */
	public PacAction getBestAction() {
		return getBestPrediction().action;
	}

	@Override
	public double completeScore() {
		RecursivePacPredictor bestPrediction = getBestPrediction();
		return localScore + (bestPrediction==null ? 0 :bestPrediction.completeScore());
	}

	@Override
	public int depth() {
		return getBestPrediction() == null ? deepness : getBestPrediction().depth();
	}

	private RecursivePacPredictor getBestPrediction() {
		if(children.isEmpty()) {
			return null;
		} else if(!(children.get(0) instanceof PacPredictor)) {
			return null;
		} else {
			return (RecursivePacPredictor) children.get(0);
		}
	}

	@Override
	public String toString() {
		return toString("\t").toString();
	}

	@Override
	public StringBuilder toString(String prefix) {
		StringBuilder sOut = new StringBuilder();
		sOut.append(prefix).append("s=").append(completeScore()).append(";").append("l=").append(localScore).append(";")
				.append(action).append("\n");
		for (PacPredictor p : children) {
			sOut.append(p.toString(prefix + "\t"));
		}
		return sOut;
	}
}
