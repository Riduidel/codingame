package org.ndx.codingame.coders_strike_back;

import java.util.Deque;

import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.Point;

public class DrifterTrajectory extends AbstractTrajectory implements Trajectory {
	public DrifterTrajectory(Position currentPosition, Position previousPosition,
			Deque<Position> targetPosition, int distance, int angle) {
		super(currentPosition, previousPosition, targetPosition, distance, angle);
		if(distance<400) {
			if(path.size()>0) {
				this.targetPosition = path.pop();
			}
		}
	}

	protected String build(Configuration config, Line direction,
			int thrust, double orthogonalDistanceToTarget) {
		if(orthogonalDistanceToTarget<=1000) {
			return buildAiming(config, direction, thrust);
		} else {
			return buildTurning(config, direction, thrust);
		}
	}

	protected String buildTurning(Configuration config, Line direction, int thrust) {
		Position aimed = targetPosition;
		System.err.println(String.format("aiming to %s", aimed));
		int absAngle = Math.abs(angle);
		if(absAngle<45) {
			System.err.println(String.format("Not aiming at target (%s)", targetPosition));
			// target is not aligned with current line ...
			// Find projection of target on line
			Position projected = direction.project(targetPosition, targetPosition);
			// Then symetric of that point through line going to target
			Line axis = new Line(currentPosition, targetPosition);
			Point symetric = axis.symetricOf(projected);
			Line perpendicular = new Line(projected, symetric);
			aimed = perpendicular.pointAtNTimes(0.75, projected);
			System.err.println(String.format("Aiming at corrected destination %s", aimed));
		}
		return aimed.goTo(thrust);
	}
}