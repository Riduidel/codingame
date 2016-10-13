package org.ndx.codingame.hypersonic;
import java.util.Scanner;

import org.ndx.codingame.gaming.Delay;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
public class Player {
	private static final int[] HOLD = {0, 0}; 
	private static final int[] LEFT = {-1, 0}; 
	private static final int[] RIGHT = {1, 0}; 
	private static final int[] TOP = {0, -1}; 
	private static final int[] DOWN = {0, 1}; 
	public static final int[][] DIRECTIONS = new int[][] {
		LEFT,
		RIGHT,
		TOP,
		DOWN
	};
	public static final int[][] POSSIBLE_DIRECTIONS = new int[][] {
//		HOLD,
		LEFT,
		RIGHT,
		TOP,
		DOWN
	};
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int width = in.nextInt();
		int height = in.nextInt();
		EvolvableConstants constants = new EvolvableConstants();
		int MAXIMUM_TRAJECTORIES = constants.COUNT_ENOUGH_TRAJECTORIES;
		Playground playground = new Playground(width, height);
		int myId = in.nextInt();
		in.nextLine();
		// game loop
		while (true) {
			playground.clear();
			Delay delay = new Delay();
			for (int rowIndex = 0; rowIndex < height; rowIndex++) {
				String row = in.nextLine();
				playground.readRow(row, rowIndex);
			}
			int entities = in.nextInt();
			Gamer me = null;
			for (int i = 0; i < entities; i++) {
				int entityType = in.nextInt();
				int owner = in.nextInt();
				int x = in.nextInt();
				int y = in.nextInt();
				int param1 = in.nextInt();
				int param2 = in.nextInt();
				switch (entityType) {
				case 0:
					// bomber
					if(owner==0)
						me = new Gamer(owner, x, y, param1, param2);
					else
						playground.readGameEntities(new Gamer(owner, x, y, param1, param2));
					break;
				case 1:
					// bomb
					playground.readGameEntities(new Bomb(owner, x, y, param1, param2));
					break;
				case 2:
					// item
					playground.readGameEntities(new Item(owner, x, y, param1, param2));
					break;
				}
			}
			in.nextLine();
			TrajectoryBuilder trajectoryBuilder = new TrajectoryBuilder(playground, delay, constants);
			Trajectory best = trajectoryBuilder
					.findBest(me);
			if(trajectoryBuilder.count<constants.COUNT_ENOUGH_TRAJECTORIES) {
				System.err.println("There was not enough trajectories computed .. Computing even less next turn");
				constants.COUNT_ENOUGH_TRAJECTORIES = Math.max(100, 
						Math.min(constants.COUNT_ENOUGH_TRAJECTORIES, trajectoryBuilder.count/constants.ADAPATION_FACTOR));
			} else {
				if(delay.howLong()<constants.DELAY_CREATE_TRAJECTORIES/2) {
					System.err.println("We computed that really fast. Computing more");
					constants.COUNT_ENOUGH_TRAJECTORIES = Math.min(MAXIMUM_TRAJECTORIES, 
							constants.COUNT_ENOUGH_TRAJECTORIES*constants.ADAPATION_FACTOR);
					
				}
			}
			
			System.err.println("It took "+delay.howLong());
//			System.err.println(playground.toString());

			System.out.println(best.toCommandString());
		}
	}
}