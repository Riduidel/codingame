package org.ndx.codingame.coders_strike_back.entities;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import org.ndx.codingame.coders_strike_back.Player;
import org.ndx.codingame.coders_strike_back.actions.Trajectory;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Line;

public class Pod {

	public Position position;
	private final ContinuousPoint speed;
	private final int podAngle;
	private final int nextCheckpointId;

	public Pod(final Position continuousPoint, final ContinuousPoint continuousPoint2, final int angle, final int nextInt) {
		position = continuousPoint;
		speed = continuousPoint2;
		podAngle = angle;
		nextCheckpointId = nextInt;
	}

	public String build(final List<Position> CHECKPOINTS, final Position previousPosition, final Collection<Line> enemyDirections, final boolean canBoost, final boolean canShield) {
		System.err.println("building strategy for "+this);
		final Deque<Position> targets = new ArrayDeque<>();
		for (int index = 0; index < CHECKPOINTS.size(); index++) {
			targets.add(CHECKPOINTS.get((nextCheckpointId+index)%CHECKPOINTS.size()));
		}
		final Position destination = targets.getFirst();
		final int nextCheckpointDist = (int) position.distance2To(destination);
		int angle = 0;
		final Line direction = new Line(position, destination);
		angle = (int) (podAngle-direction.angle());
		if(angle>180) {
			angle = angle-360;
		}
		final Trajectory builder = Player.using().position(position)
				.previous(previousPosition)
				.targets(targets)
				.distance(nextCheckpointDist)
				.angle(angle)
				.build();
		return builder.build(canBoost, canShield, enemyDirections);
	}

	@Override
	public String toString() {
		return "Pod [position=" + position + ", speed=" + speed + ", angle=" + podAngle + ", nextCheckpointId="
				+ nextCheckpointId + "]";
	}
	
}