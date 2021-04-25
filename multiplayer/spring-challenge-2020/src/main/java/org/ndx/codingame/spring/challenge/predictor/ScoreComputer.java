package org.ndx.codingame.spring.challenge.predictor;

import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.actions.PacAction;
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

public class ScoreComputer extends ContentAdapter<Double> {

	private PacAction action;
	private int deepness;
	private AbstractPac my;

	public ScoreComputer(AbstractPac my, PacAction pacAction, int deepness) {
		super(0.0);
		this.action = pacAction;
		this.deepness = deepness;
		this.my = my;
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
						return (double) EvolvableConstants.INTERNAL_SCORE_FOR_NON_DANGEROUS;
					} else if (pac.abilityCooldown < deepness) {
						action.withMessage("unsure;");
						return (double) EvolvableConstants.INTERNAL_SCORE_FOR_NON_DANGEROUS;
					} else {
						action.withMessage("sure;");
						return (double) EvolvableConstants.INTERNAL_SCORE_FOR_ENEMY_PREY;
					}
				} else {
					action.withMessage("on same;");
					return (double) EvolvableConstants.INTERNAL_SCORE_FOR_NON_DANGEROUS;
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