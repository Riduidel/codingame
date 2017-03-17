package org.ndx.codingame.codevszombies;

import java.util.Scanner;

import org.ndx.codingame.codevszombies.entities.Ash;
import org.ndx.codingame.codevszombies.entities.Human;
import org.ndx.codingame.codevszombies.entities.Zombie;
import org.ndx.codingame.codevszombies.playground.Playfield;

public class Player {

    public static void main(final String args[]) {
        final Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
        	final Playfield playground = new Playfield();
            final int x = in.nextInt();
            final int y = in.nextInt();
            playground.add(new Ash(x, y));
            final int humanCount = in.nextInt();
            for (int i = 0; i < humanCount; i++) {
                final int humanId = in.nextInt();
                final int humanX = in.nextInt();
                final int humanY = in.nextInt();
                playground.add(new Human(humanId, humanX, humanY));
            }
            final int zombieCount = in.nextInt();
            for (int i = 0; i < zombieCount; i++) {
                final int zombieId = in.nextInt();
                final int zombieX = in.nextInt();
                final int zombieY = in.nextInt();
                final int zombieXNext = in.nextInt();
                final int zombieYNext = in.nextInt();
                playground.add(new Zombie(zombieId, zombieX, zombieY, zombieXNext, zombieYNext));
            }

            System.err.println(playground.toUnitTestString());
            System.out.println(playground.compute()); // Your destination coordinates
        }
    }
}