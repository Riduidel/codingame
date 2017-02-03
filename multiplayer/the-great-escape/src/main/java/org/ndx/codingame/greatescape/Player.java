package org.ndx.codingame.greatescape;
import java.util.Scanner;

import org.ndx.codingame.greatescape.entities.Gamer;
import org.ndx.codingame.greatescape.entities.Orientation;
import org.ndx.codingame.greatescape.entities.Wall;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
public class Player {

    private static final Direction[] DIRECTIONS = new Direction[] {
                                                                Direction.RIGHT,
                                                                Direction.LEFT,
                                                                Direction.DOWN,
                                                                Direction.UP
    };

	public static void main(final String args[]) {
        final Scanner in = new Scanner(System.in);
        final int WIDTH = in.nextInt(); // width of the board
        final int HEIGHT = in.nextInt(); // height of the board
        final int playerCount = in.nextInt(); // number of players (2 or 3)
        final int myId = in.nextInt(); // id of my player (0 = 1st player, 1 = 2nd player, ...)

        Gamer me = null;
        // game loop
        while (true) {
        	// As walls are put between cells, please use a "convenient" playfield size
        	// Notice this is handled directly by the playfield class !
        	final Playfield used = new Playfield(WIDTH, HEIGHT);
            for (int i = 0; i < playerCount; i++) {
                final int x = in.nextInt(); // x-coordinate of the player
                final int y = in.nextInt(); // y-coordinate of the player
                final int wallsLeft = in.nextInt(); // number of walls available for the player
                final Gamer gamer = new Gamer(x, y, wallsLeft, DIRECTIONS[i]);
                if(i==myId) {
					me = gamer;
				}
                // Do not account for eliminated players
                if(x>=0 && y>=0) {
					used.setAt(x, y, gamer);
				}
//                System.err.println(String.format("new Gamer(%d, %d)", x, y));
            }
            final int wallCount = in.nextInt(); // number of walls on the board
            for (int i = 0; i < wallCount; i++) {
                final int x = in.nextInt(); // x-coordinate of the wall
                final int y = in.nextInt(); // y-coordinate of the wall
                final String wallOrientation = in.next(); // wall orientation ('H' or 'V')
                used.setAt(x, y, new Wall(Orientation.valueOf(wallOrientation)));
                //               System.err.println(String.format("new Wall(%d, %d, %s)", x, y, wallOrientation));
            }

            // Write an action using System.out.println()
            System.err.println(used.toUnitTestString(me));


            // action: LEFT, RIGHT, UP, DOWN or "putX putY putOrientation" to place a wall
            System.out.println(me.compute(used).toCodingame());
        }
    }
}