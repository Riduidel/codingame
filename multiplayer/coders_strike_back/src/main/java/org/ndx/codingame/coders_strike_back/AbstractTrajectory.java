package org.ndx.codingame.coders_strike_back;

import java.util.Collection;
import java.util.Deque;

import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.base.AbstractPoint;

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
	
	protected int computeThrust(Line direction, AbstractPoint target) {
		double angularThrust = computeAngularThrust();
		double distanceThrust = computeDistanceThrust();
//		System.err.println(String.format("Angular thrust is %f, distance thrust is %f", angularThrust, distanceThrust));
		return (int) (angularThrust*distanceThrust*Player.MAXIMUM_THRUST);
	}

	protected float computeDistanceThrust() {
		return distance>1000 ? 1 : distance/1000f;
	}

	protected int computeAngularThrust() {
		return Math.abs(angle)<60 ? 1 : 0;
	}

	@Override
	public String build(boolean canBoost, boolean canShield, Collection<Line> enemyDirections) {
		Line mypodDirection = new Line(previousPosition, currentPosition);
		String thrust = computeThrust(mypodDirection, targetPosition)+"";
		double distanceTo = mypodDirection.distance2To(targetPosition);
//		System.err.println(String.format("angle to target is %s distance to target is %s orthogonal distance is %s", angle, distance, distanceTo));
		if(canShield) {
			for(Line enemy : enemyDirections) {
				if(enemy.second.distance2To(currentPosition)<500*2) {
					thrust = "SHIELD";
				}
			}
		}
		return build(mypodDirection, thrust, distanceTo);
	}

	protected abstract String build(Line mypodDirection, String thrust, double distanceTo);

	protected String buildAiming(Line direction, String thrust) {
		System.err.println("Aiming directly at target !");
		return targetPosition.goTo(thrust);
	}
	
}