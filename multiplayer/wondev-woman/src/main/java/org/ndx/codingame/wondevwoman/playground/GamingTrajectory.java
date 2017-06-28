package org.ndx.codingame.wondevwoman.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ndx.codingame.wondevwoman.actions.WonderAction;

public class GamingTrajectory implements Comparable<GamingTrajectory>{
	private double score = 0;
	private final List<GamingStep> steps = new ArrayList<>();
	public GamingTrajectory() {
	}
	public GamingTrajectory(final GamingStep step) {
		this();
		steps.add(step);
	}
	@Override
	public int compareTo(final GamingTrajectory o) {
		return (int) Math.signum(getScore()-o.getScore());
	}
	private double getScore() {
		if(score==0) {
			double temporary = 0;
			for(final GamingStep s : steps) {
				temporary+=s.getScore();
			}
			score = temporary/steps.size();
		}
		return 0;
	}
	public String toCommandString() {
		return steps.get(0).getApplied().toCommandString();
	}
	public Collection<GamingTrajectory> extend() {
		final GamingStep last = steps.get(steps.size()-1);
		final List<WonderAction> actions = last.computeAvailableActions();
		final Collection<GamingTrajectory> returned = new ArrayList<>();
		for(final WonderAction action : actions) {
			final GamingTrajectory trajectory = new GamingTrajectory();
			trajectory.steps.addAll(steps);
			trajectory.steps.add(last.deriveForAction(action));
			returned.add(trajectory);
		}
		return returned;
	}
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("GamingTrajectory [score=");
		builder.append(score);
		builder.append(", steps.size=");
		builder.append(steps.size());
		builder.append("]");
		return builder.toString();
	}
}
