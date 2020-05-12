package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.MutableProxy;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.actions.ActionTree;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.entities.AbstractDistinctContent;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Pac;

public class Turn extends MutableProxy<Content> implements SpringChallengePlayground {
	private Set<BigPill> bigPills = new HashSet<>();
	private Set<Pac> pacs = new HashSet<Pac>();

	public Turn(SpringChallengePlayground playfield) {
		super(playfield);
	}
	@Override
	public Turn readWriteProxy() {
		Turn returned = new Turn(this);
		for(BigPill b : bigPills) {
			returned.set(b, b);
		}
		for(Pac pac : pacs) {
			returned.set(pac, pac);
		}
		return returned;
	}


	@Override
	public void set(DiscretePoint p, Content c) {
		setSpecificContent(get(p), c);
		super.set(p, c);
	}

	private void setSpecificContent(Content previous, Content next) {
		if (next instanceof BigPill) {
			bigPills.add((BigPill) next);
		} else if (next instanceof Pac) {
			pacs.add((Pac) next);
		} else if (next instanceof Ground) {
			if(previous instanceof BigPill) {
				bigPills.remove((BigPill) previous);
			} else if (next instanceof Pac) {
				pacs.remove((Pac) previous);
			}
		}
	}
	
	@Override
	public void set(int x, int y, Content c) {
		setSpecificContent(get(x, y), c);
		super.set(x, y, c);
	}

	public String compute() {
		Map<Pac, PacAction> actions = computeActions(-1);
		return toCommands(actions);
	}

	public Map<Pac, PacAction> computeActions(int maximum) {
		List<Pac> myPacs = getMyPacs();
		Map<Pac, ActionTree> actionList = new HashMap<>();
		for (Pac pac : myPacs) {
			actionList.put(pac, new ActionTree(getSource(), this, pac, pac, 0));
		}
		int computations = 0;
		Delay delay = new Delay();
		do {
			for (Pac pac : myPacs) {
				actionList.get(pac).grow();
			}
			computations++;
		} while ((maximum<0 && !delay.isElapsed(EvolvableConstants.DELAY_FOR_PREDICTION))
				|| computations<=maximum);

		Map<Pac, PacAction> returned = new HashMap<>();
		for (Map.Entry<Pac, ActionTree> action : actionList.entrySet()) {
			ActionTree actionTree = action.getValue();
			returned.put(action.getKey(), 
					actionTree.getFirstAction()
						.withMessage(String.format("c=%d;s=%d", computations, actionTree.score())));
		}
		return returned;
	}

	private List<Pac> getMyPacs() {
		List<Pac> returned = new ArrayList<>();
		for (Pac pac : pacs) {
			if (pac.mine)
				returned.add(pac);
		}
		return returned;
	}

	private String toCommands(Map<Pac, PacAction> actions) {
		return actions.values().stream().map(action -> action.toCommandString()).collect(Collectors.joining("|"));
	}

	public void readGameEntities(AbstractDistinctContent... contents) {
		for (AbstractDistinctContent c : contents) {
			set(c, c);
		}
	}

	@Override
	public SpringChallengePlayground getSource() {
		return (SpringChallengePlayground) super.getSource();
	}

	@Override
	public List<List<DiscretePoint>> speedPointsAt(DiscretePoint p) {
		return getSource().speedPointsAt(p);
	}

	@Override
	public List<DiscretePoint> nextPointsAt(DiscretePoint p) {
		return getSource().nextPointsAt(p);
	}

	public ImmutablePlayground<Integer> getBigPillScore(Pac pac) {
		ImmutablePlayground<Integer> base = zero();
		for(BigPill b : bigPills) {
			base = base.apply(cacheDistanceMapTo(b).scores, (first, second) -> first+second);
		}
		return base;
	}

	@Override
	public Playground<Integer> zero() {
		return getSource().zero();
	}

	@Override
	public ScoringSystem cacheDistanceMapTo(BigPill pill) {
		return getSource().cacheDistanceMapTo(pill);
	}
}
