package org.ndx.codingame.coders_strike_back;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

import org.ndx.codingame.lib2d.Point;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
public class Player {
	public static final int MAXIMUM_THRUST = 100;

	public static TrajectoryBuilder using() {
		return new TrajectoryBuilder();
	}
	
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);

		List<Position> CHECKPOINTS = new ArrayList<Position>();

		Configuration config = new Configuration();
		Position previousPosition = null;
		// game loop
		while (true) {
			Position position = new Position(in.nextInt(), in.nextInt());
			Position nextCheckpoint = new Position(in.nextInt(), in.nextInt());
			Deque<Position> targets = new ArrayDeque<Position>();
			targets.add(nextCheckpoint);
			if(CHECKPOINTS.contains(nextCheckpoint)) {
				int index = CHECKPOINTS.indexOf(nextCheckpoint);
				targets.add(CHECKPOINTS.get((index+1)%CHECKPOINTS.size()));
			} else {
				CHECKPOINTS.add(nextCheckpoint);
			}
			int nextCheckpointDist = in.nextInt();
			int nextCheckpointAngle = in.nextInt();
			// unused for now
			Point opponent = new Point(in.nextInt(), in.nextInt());

			Trajectory builder = using().position(position)
									.previous(previousPosition)
									.targets(targets)
									.distance(nextCheckpointDist)
									.angle(nextCheckpointAngle)
									.build();
			System.out.println(builder.build(config));
			previousPosition = position;
		}
	}
}