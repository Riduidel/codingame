package org.ndx.codingame.coders_strike_back;

import java.util.Deque;

import org.ndx.codingame.lib2d.Point;

public class TrajectoryBuilder {

		private Position currentPosition;
		private Position previousPosition;
		private Deque<Position> targetPosition;
		private int distance;
		private int angle;

		public TrajectoryBuilder position(Position position) {
			this.currentPosition = position;
			return this;
		}

		public TrajectoryBuilder previous(Position position) {
			this.previousPosition = position;
			return this;
		}

		public TrajectoryBuilder targets(Deque<Position> position) {
			this.targetPosition = position;
			return this;
		}

		public TrajectoryBuilder distance(int nextCheckpointDist) {
			this.distance = nextCheckpointDist;
			return this;
		}

		public TrajectoryBuilder angle(int nextCheckpointAngle) {
			this.angle = nextCheckpointAngle;
			return this;
		}

		public Trajectory build() {
			if(previousPosition==null) {
				return new StartTrajectory(targetPosition.peek());
			} else {
				return new DrifterTrajectory(currentPosition, previousPosition, targetPosition, distance, angle);
//				return new TriangularTrajectory(currentPosition, previousPosition, targetPosition, distance, angle);
			}
		}
		
	}