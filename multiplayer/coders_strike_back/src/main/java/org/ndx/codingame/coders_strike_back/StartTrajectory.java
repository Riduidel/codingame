package org.ndx.codingame.coders_strike_back;

import java.util.Collection;

import org.ndx.codingame.lib2d.Line;

public class StartTrajectory implements Trajectory {
	private final Position target;
	public StartTrajectory(Position target) {
		super();
		this.target = target;
	}
	@Override
	public String build(boolean canBoost, boolean canShield, Collection<Line> enemyDirections) {
		if(canBoost)
			return target.goTo("BOOST");
		else
			return target.goTo(100);
	}
}