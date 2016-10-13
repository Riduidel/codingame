package org.ndx.codingame.hypersonic;

import java.util.ArrayList;
import java.util.List;

public class Trajectory {
	public int score;
	public final List<Step> steps = new ArrayList<>(EvolvableConstants.HORIZON);
	public void add(Step next, int speedBonus) {
		steps.add(next);
		score += next.score*speedBonus;
	}
	public String toCommandString() {
		return steps.get(0).toCommandString();
	}
}