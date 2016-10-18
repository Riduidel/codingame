package org.ndx.codingame.coders_strike_back;

public class StartTrajectory implements Trajectory {
	private final Position target;
	public StartTrajectory(Position target) {
		super();
		this.target = target;
	}
	@Override
	public String build(Configuration config) {
		return target.goTo("BOOST");
	}
}