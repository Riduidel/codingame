package org.ndx.codingame.coders_strike_back;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

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
				secondPreviousPosition = null,
				firstEnemyPreviousPosition = null,
				secondEnemyPreviousPosition = null;
		
		// game loop
		while (true) {
			Pod myfirst = readpod(in);
			Pod mysecond = readpod(in);
			Pod opponentfirst = readpod(in);
			Pod opponentsecond = readpod(in);
			
			Collection<Line> enemyDirections = new ArrayList<>(2);
			if(firstEnemyPreviousPosition!=null)
				enemyDirections.add(new Line(firstEnemyPreviousPosition, opponentfirst.position));
			if(secondEnemyPreviousPosition!=null)
				enemyDirections.add(new Line(secondEnemyPreviousPosition, opponentsecond.position));

			System.out.println(myfirst.build(CHECKPOINTS, firstPreviousPosition, enemyDirections, true, false));
			System.out.println(mysecond.build(CHECKPOINTS, secondPreviousPosition, enemyDirections, false, true));
			firstPreviousPosition = myfirst.position;
			secondPreviousPosition = mysecond.position;
			firstEnemyPreviousPosition = opponentfirst.position;
			secondEnemyPreviousPosition = opponentsecond.position;
		}
	}

	private static Pod readpod(Scanner in) {
		return new Pod(new Position(in.nextDouble(), in.nextDouble()),
				new ContinuousPoint(in.nextDouble(), in.nextDouble()),
				in.nextInt(),
				in.nextInt());
	}
}