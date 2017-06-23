package org.ndx.codingame.wondevwoman;

import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
public class Player {

	public static void main(final String args[]) {
		final Scanner in = new Scanner(System.in);
		final int size = in.nextInt();
		final int unitsPerPlayer = in.nextInt();

		// game loop
		while (true) {
			for (int i = 0; i < size; i++) {
				final String row = in.next();
			}
			for (int i = 0; i < unitsPerPlayer; i++) {
				final int unitX = in.nextInt();
				final int unitY = in.nextInt();
			}
			for (int i = 0; i < unitsPerPlayer; i++) {
				final int otherX = in.nextInt();
				final int otherY = in.nextInt();
			}
			final int legalActions = in.nextInt();
			for (int i = 0; i < legalActions; i++) {
				final String atype = in.next();
				final int index = in.nextInt();
				final String dir1 = in.next();
				final String dir2 = in.next();
			}

			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");

			System.out.println("MOVE&BUILD 0 N S");
		}
	}
}