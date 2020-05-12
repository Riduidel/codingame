package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.MutablePlayground;
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
	private ImmutablePlayground<Integer> bigPillDistances;

	public Turn(SpringChallengePlayground playfield, ImmutablePlayground<Integer> bigPills) {
		super(playfield);
		this.bigPillDistances = bigPills;
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

	@Override
	public Playground<Integer> zero() {
		return getSource().zero();
	}

	@Override
	public ScoringSystem cacheDistanceMapTo(BigPill pill) {
		return getSource().cacheDistanceMapTo(pill);
	}

	public Integer getBigPillScore(Pac pac) {
		return bigPillDistances.get(pac);
	}

	@Override
	public ImmutablePlayground<Integer> getBigPillsDistances() {
		return bigPillDistances;
	}
}
