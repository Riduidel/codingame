package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
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
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.entities.VirtualPac;

public class PacPredictor {

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
		public Double visitPac(Pac pac) {
			if(pac.type!=Type.DEAD) {
				if(pac.mine) {
					// On first predictor run, action is null
					if(action!=null)
						action.withMessage("on my pac;");
					if(pac.id==my.id) {
						return 0.0;
					} else {
						return (double) EvolvableConstants.INTERNAL_SCORE_FOR_TEAMMATE_TOO_CLOSE;
					}
				} else {
					if(pac.isDangerousFor(my)) {
						action.withMessage("on predator;");
						return (double) EvolvableConstants.INTERNAL_SCORE_FOR_ENEMY_PREDATOR;
					} else if(my.isDangerousFor(pac)) {
						// Take care and only attack enemies which can't mutate
						action.withMessage("on prey;");
						if(pac.abilityCooldown<deepness) {
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
			if(pacTrace.mine) {
				action.withMessage("on my pac trac");
			} else {
				if(pacTrace.isDangerousFor(my)) {
					action.withMessage("on predator pac trace");
					return -1.0*EvolvableConstants.INTERNAL_SCORE_FOR_PAC_TRACE;
				} else {
					action.withMessage("on prey pac trace");
					return (double) EvolvableConstants.INTERNAL_SCORE_FOR_PAC_TRACE;
				}
			}
			return 0.0;
		}
	}
	private MutablePlayground<Content> playfield;
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
	 * List of possible actions we will evaluate here.
	 * It is built at construction time and consumed by the grow method
	 */
	private List<PacAction> possibleActions = new ArrayList<>();
	private boolean deadEnd;
	private int deepness;
	private double localScore;
	private PacPredictor bestPrediction;
	private int iterator = 0;

	public PacPredictor(MutablePlayground<Content> playfield,
			Cache cache,
			AbstractPac pac,
			DiscretePoint origin,
			int deepness,
			PacAction action) {
		this.playfield = playfield;
		this.cache = cache;
		this.my = pac;
		this.action = action;
		if(deepness==0) {
			if(pac.abilityCooldown==0) {
				possibleActions.add(new Speed(pac));
				for(Type type : Type.values()) {
					if(type!=Type.DEAD && type!=pac.type) {
						possibleActions.add(new Switch(pac, type));
					}
				}
			}
		}
		this.deepness = deepness+1;
		List<MoveTo> moves = createMoveActions(origin);
		deadEnd = moves.isEmpty();
		possibleActions.addAll(moves);
		this.localScore = computeScore()/(this.deepness*this.deepness);
		if(deadEnd) {
			this.localScore*=EvolvableConstants.DEADEND_BONUS;
			action.withMessage("DEADEND;");
		}
		playfield.set(pac, pac);
	}

	private double computeScore() {
		Content content = playfield.get(my);
		return content.accept(new ScoreComputer(0.0));
	}

	/**
	 * This method emits true when 
	 * @param origin
	 * @return
	 */
	private List<MoveTo> createMoveActions(DiscretePoint origin) {
		List<List<DiscretePoint>> nextPoints = cache.getNextPointsCache(my);
		List<MoveTo> returned = new ArrayList<>();
		// Remove the list of points the origin is in, to avoid looping predictions
		for(List<DiscretePoint> navigable : nextPoints) {
			if(!navigable.contains(origin)) {
				// As we're building real move actions, let's evaluate only those which provide real moves
				DiscretePoint goal = null;
				for(DiscretePoint p : navigable) {
					if(playfield.get(p).canBeWalkedOnBy(my)) {
						goal = p;
					}
				}
				if(goal!=null && !goal.equals(my)) {
					returned.add(new MoveTo(my, goal));
				}
			}
		}
		return returned;
	}

	/**
	 * Grow that pac predictor of one more pac prediction
	 * @param iteration 
	 * @return 
	 */
	public boolean grow(int iteration) {
		if(deepness>EvolvableConstants.HORIZON_FOR_RANDOM_PATH)
			return false;
		boolean continueToGrow = false;
		PacPredictor evaluated = null;
		if(possibleActions.isEmpty()) {
			if(children.size()>0) {
				evaluated = children.get(iterator++%children.size());
				continueToGrow = evaluated.grow(iteration);
			}
		} else {
			PacAction action = possibleActions.remove(iterator%possibleActions.size());
			action.withMessage("i="+iteration+";");
			playfield.set(my, 
					new VirtualPac(my.x, my.y, my.id, my.mine, my.type, my.speedTurnsLeft, my.abilityCooldown,
							playfield.get(my)));
			AbstractPac transformed = action.transform(playfield);
			// We make sure playfield still allow that move
			// to avoid locking of pacs
			if(playfield.get(transformed).canBeWalkedOnBy(transformed)) {
				evaluated = new PacPredictor(
						playfield,
						cache,
						transformed, 
						// This one is really a hack: setting origin to null will allow non-move actions to be handled properly
						action instanceof MoveTo ? new DiscretePoint(my.x, my.y) : null, 
						deepness, 
						action);
				children.add(evaluated);
			}
			continueToGrow = true;
		}
		if(evaluated!=null) {
			if(bestPrediction==null) {
				bestPrediction = evaluated;
			} else if(evaluated.completeScore()>bestPrediction.completeScore()) {
				bestPrediction = evaluated;
			}
		}
		return continueToGrow;
	}
	
	public PacAction getBestAction() {
		return bestPrediction.action.withMessage("d="+depth()+";s="+completeScore()+";");
	}

	private double completeScore() {
		return localScore+(bestPrediction==null ? 0 : bestPrediction.completeScore());
	}

	private int depth() {
		return bestPrediction==null ? deepness : bestPrediction.depth();
	}

	@Override
	public String toString() {
		return toString("\t").toString();
	}
	private StringBuilder toString(String prefix) {
		StringBuilder sOut = new StringBuilder();
		sOut.append(prefix)
			.append("s=").append(completeScore()).append(";")
			.append("l=").append(localScore).append(";")
			.append(action).append("\n");
		for(PacPredictor p : children) {
			sOut.append(p.toString(prefix+"\t"));
		}
		return sOut;
	}
}
