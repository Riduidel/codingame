package org.ndx.codingame.coders_strike_back;

import java.util.Deque;

import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

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

	protected String build(Line mypodDirection, String thrust,
			double orthogonalDistanceToTarget) {
		if(orthogonalDistanceToTarget<=1000) {
			return buildAiming(mypodDirection, thrust);
		} else {
			return buildTurning(mypodDirection, thrust);
		}
	}

	protected String buildTurning(Line mypodDirection, String thrust) {
		Position aimed = targetPosition;
//		System.err.println(String.format("aiming to %s", aimed));
		int absAngle = Math.abs(angle);
		if(absAngle<45) {
//			System.err.println(String.format("Not aiming at target (%s)", targetPosition));
			// target is not aligned with current line ...
			// Find projection of target on line
			Position projected = mypodDirection.project(targetPosition, targetPosition);
			// Then symetric of that point through line going to target
			Line axis = new Line(currentPosition, targetPosition);
			ContinuousPoint symetric = axis.symetricOf(projected);
			Line perpendicular = new Line(projected, symetric);
			aimed = perpendicular.pointAtNTimes(trajectoryCorrector(mypodDirection, axis, distance), projected);
//			System.err.println(String.format("Aiming at corrected destination %s", aimed));
		}
		return aimed.goTo(thrust);
	}
	
	@Override
	protected float computeDistanceThrust() {
		return 1;
	}

	protected double trajectoryCorrector(Line mypodDirection, Line axis, int distance) {
		if(distance<=1000)
			return 1;
		else if(distance<=2000)
			return 0.9;
		else if(distance<=3000)
			return 0.8;
		else if(distance<=4000)
			return 0.7;
		else if(distance<=5000)
			return 0.6;
		else 
			return 0.5;
//		return 0.75;
	}
}