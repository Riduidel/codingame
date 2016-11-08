package org.ndx.codingame.coders_strike_back;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
public class Player {
	public static class Pod {

		private Position position;
		private ContinuousPoint speed;
		private int podAngle;
		private int nextCheckpointId;

		public Pod(Position continuousPoint, ContinuousPoint continuousPoint2, int angle, int nextInt) {
			this.position = continuousPoint;
			this.speed = continuousPoint2;
			this.podAngle = angle;
			this.nextCheckpointId = nextInt;
		}

		public String build(List<Position> CHECKPOINTS, Position previousPosition) {
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
			Trajectory builder = using().position(position)
					.previous(previousPosition)
					.targets(targets)
					.distance(nextCheckpointDist)
					.angle(angle)
					.build();
			return builder.build();
		}

		@Override
		public String toString() {
			return "Pod [position=" + position + ", speed=" + speed + ", angle=" + podAngle + ", nextCheckpointId="
					+ nextCheckpointId + "]";
		}
		
	}
	public static final int MAXIMUM_THRUST = 100;

	public static TrajectoryBuilder using() {
		return new TrajectoryBuilder();
	}
	
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);

		int laps = in.nextInt();
		int checkpoints = in.nextInt();
		int index = 0;
		List<Position> CHECKPOINTS = new ArrayList<Position>();
		while(index<checkpoints) {
			CHECKPOINTS.add(new Position(in.nextDouble(), in.nextDouble()));
			index++;
		}

		System.err.println(String.format("%d laps to do around checkpoints %s", laps, CHECKPOINTS));
		Position firstPreviousPosition = null,
				secondPreviousPosition = null;
		
		// game loop
		while (true) {
			Pod myfirst = readpod(in);
			Pod mysecond = readpod(in);
			Pod opponentfirst = readpod(in);
			Pod opponentsecond = readpod(in);

			System.out.println(myfirst.build(CHECKPOINTS, firstPreviousPosition));
			System.out.println(mysecond.build(CHECKPOINTS, secondPreviousPosition));
			firstPreviousPosition = myfirst.position;
			secondPreviousPosition = mysecond.position;
		}
	}

	private static Pod readpod(Scanner in) {
		return new Pod(new Position(in.nextDouble(), in.nextDouble()),
				new ContinuousPoint(in.nextDouble(), in.nextDouble()),
				in.nextInt(),
				in.nextInt());
	}
}