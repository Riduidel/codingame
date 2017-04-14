package org.ndx.codingame.carribeancoders;

import java.util.Scanner;

import org.ndx.codingame.carribeancoders.entities.Barrel;
import org.ndx.codingame.carribeancoders.entities.Cannonball;
import org.ndx.codingame.carribeancoders.entities.Mine;
import org.ndx.codingame.carribeancoders.entities.Ship;
import org.ndx.codingame.carribeancoders.playground.Playfield;
import org.ndx.codingame.gaming.tounittest.ToUnitTestStringBuilder;

public class Player {
	public static void main(final String args[]) {
        final Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
        	final Playfield playfield = new Playfield();
            final int myShipCount = in.nextInt(); // the number of remaining ships
            final int entityCount = in.nextInt(); // the number of entities (e.g. ships, mines or cannonballs)
            for (int i = 0; i < entityCount; i++) {
                final int entityId = in.nextInt();
                final String entityType = in.next();
                final int x = in.nextInt();
                final int y = in.nextInt();
                final int arg1 = in.nextInt();
                final int arg2 = in.nextInt();
                final int arg3 = in.nextInt();
                final int arg4 = in.nextInt();
                switch(entityType) {
                case "SHIP":
                	playfield.add(new Ship(x, y, arg1, arg2, arg3, arg4));
                	break;
                case "BARREL":
                	playfield.add(new Barrel(x, y, arg1));
                	break;
                case "CANNONBALL":
                	playfield.add(new Cannonball(x, y, arg1, arg2));
                	break;
                case "MINE":
                	playfield.add(new Mine(x, y));
                	break;
                }
            }
            System.err.println(new ToUnitTestStringBuilder("can_find_moves").build(playfield));
            System.out.println(playfield.movesToCommand());
        }
    }
}