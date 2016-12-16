package org.ndx.codingame.coders_strike_back.actions;

import java.util.Deque;

import org.ndx.codingame.coders_strike_back.entities.Position;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Line;

public class DrifterTrajectory extends AbstractTrajectory implements Trajectory {
	public DrifterTrajectory(final Position currentPosition, final Position previousPosition,
			final Deque<Position> targetPosition, final int distance, final int angle) {
		super(currentPosition, previousPosition, targetPosition, distance, angle);
		if(distance<400) {
			if(path.size()>0) {
				this.targetPosition = path.pop();
			}
		}
	}

	@Override
	protected String build(final Line mypodDirection, final String thrust,
			final double orthogonalDistanceToTarget) {
		if(orthogonalDistanceToTarget<=1000) {
			return buildAiming(mypodDirection, thrust);
		} else {
			return buildTurning(mypodDirection, thrust);
		}
	}

	protected String buildTurning(final Line mypodDirection, final String thrust) {
		Position aimed = targetPosition;
//		System.err.println(String.format("aiming to %s", aimed));
		final int absAngle = Math.abs(angle);
		if(absAngle<45) {
//			System.err.println(String.format("Not aiming at target (%s)", targetPosition));
			// target is not aligned with current line ...
			// Find projection of target on line
			final Position projected = (Position) mypodDirection.project(targetPosition, targetPosition);
			// Then symetric of that point through line going to target
			final Line axis = new Line(currentPosition, targetPosition);
			final ContinuousPoint symetric = axis.symetricOf(projected);
			final Line perpendicular = new Line(projected, symetric);
			aimed = (Position) perpendicular.pointAtNTimes(trajectoryCorrector(mypodDirection, axis, distance), projected);
//			System.err.println(String.format("Aiming at corrected destination %s", aimed));
		}
		return aimed.goTo(thrust);
	}
	
	@Override
	protected float computeDistanceThrust() {
		return 1;
	}

	protected double trajectoryCorrector(final Line mypodDirection, final Line axis, final int distance) {
		if(distance<=1000) {
			return 1;
		} else if(distance<=2000) {
			return 0.9;
		} else if(distance<=3000) {
			return 0.8;
		} else if(distance<=4000) {
			return 0.7;
		} else if(distance<=5000) {
			return 0.6;
		}
		else {
			return 0.5;
//		return 0.75;
		}
	}
}