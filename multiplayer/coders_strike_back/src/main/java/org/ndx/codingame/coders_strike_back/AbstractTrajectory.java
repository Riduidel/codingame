package org.ndx.codingame.coders_strike_back;

import java.util.Deque;

import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.Point;

public abstract class AbstractTrajectory implements Trajectory {

	protected final Position currentPosition;
	protected final Position previousPosition;
	protected final Deque<Position> path;
	protected final int angle;
	protected final int distance;
	protected Position targetPosition;

	public AbstractTrajectory(Position currentPosition,
			Position previousPosition, Deque<Position> path, int distance,
			int angle) {
		super();
		this.currentPosition = currentPosition;
		this.previousPosition = previousPosition;
		this.path = path;
		this.angle = angle;
		this.distance = distance;
		this.targetPosition = path.pop();
	}
	
	protected int computeThrust(Line direction, Point target) {
		double angularThrust = Math.abs(angle)<60 ? 1 : 0;
		double distanceThrust = distance>1000 ? 1 : distance/1000f;
		return (int) (angularThrust*distanceThrust*Player.MAXIMUM_THRUST);
	}

	@Override
	public String build(Configuration config) {
		Line direction = new Line(previousPosition, currentPosition);
		int thrust = computeThrust(direction, targetPosition);
		double distanceTo = direction.distance2To(targetPosition);
		System.err.println(String.format("angle to target is %s distance to target is %s orthogonal distance is %s", angle, distance, distanceTo));
		return build(config, direction, thrust, distanceTo);
	}

	protected abstract String build(Configuration config, Line direction, int thrust,
			double distanceTo);

	protected String buildAiming(Configuration config, Line direction, int thrust) {
		System.err.println("Aiming directly at target !");
		return targetPosition.goTo(thrust);
	}
	
}