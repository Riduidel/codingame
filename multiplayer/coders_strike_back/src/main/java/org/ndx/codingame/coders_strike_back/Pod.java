package org.ndx.codingame.coders_strike_back;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Pod {

	Position position;
	private ContinuousPoint speed;
	private int podAngle;
	private int nextCheckpointId;

	public Pod(Position continuousPoint, ContinuousPoint continuousPoint2, int angle, int nextInt) {
		this.position = continuousPoint;
		this.speed = continuousPoint2;
		this.podAngle = angle;
		this.nextCheckpointId = nextInt;
	}

	public String build(List<Position> CHECKPOINTS, Position previousPosition, Collection<Line> enemyDirections, boolean canBoost, boolean canShield) {
		System.err.println("building strategy for "+this);
		Deque<Position> targets = new ArrayDeque<>();
		for (int index = 0; index < CHECKPOINTS.size(); index++) {
			targets.add(CHECKPOINTS.get((nextCheckpointId+index)%CHECKPOINTS.size()));
		}
		Position destination = targets.getFirst();
		int nextCheckpointDist = (int) position.distance2To(destination);
		int angle = 0;
		Line direction = new Line(position, destination);
		angle = (int) (podAngle-direction.angle());
		if(angle>180) {
			angle = angle-360;
		}
		Trajectory builder = Player.using().position(position)
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