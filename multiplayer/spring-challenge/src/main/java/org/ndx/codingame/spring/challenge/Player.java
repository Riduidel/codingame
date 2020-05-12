package org.ndx.codingame.spring.challenge;
import java.util.Scanner;

import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.playground.Playfield;
import org.ndx.codingame.spring.challenge.playground.Turn;

/**
 * Grab the pellets as fast as you can!
 **/
public class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // size of the grid
        int height = in.nextInt(); // top left corner is (x=0, y=0)
        if (in.hasNextLine()) {
            in.nextLine();
        }
        Playfield playfield = new Playfield(width, height);
        for (int i = 0; i < height; i++) {
            playfield.readRow(in.nextLine(), i); // one line of the grid: space " " is floor, pound "#" is wall
        }
        playfield.init();

        // game loop
        while (true) {
        	playfield.advanceOneTurn();
            int myScore = in.nextInt();
            int opponentScore = in.nextInt();
            int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight
            for (int i = 0; i < visiblePacCount; i++) {
                int pacId = in.nextInt(); // pac number (unique within a team)
                boolean mine = in.nextInt() != 0; // true if this pac is yours
                int x = in.nextInt(); // position in the grid
                int y = in.nextInt(); // position in the grid
                String typeId = in.next(); // unused in wood leagues
                int speedTurnsLeft = in.nextInt(); // unused in wood leagues
                int abilityCooldown = in.nextInt(); // unused in wood leagues
                playfield.set(x, y, new Pac(x, y, pacId, mine, Type.valueOf(typeId), speedTurnsLeft, abilityCooldown));
            }
            int visiblePelletCount = in.nextInt(); // all pellets in sight
            for (int i = 0; i < visiblePelletCount; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                int value = in.nextInt(); // amount of points this pellet is worth
                if(value==1) {
                    playfield.set(x, y, new SmallPill(x, y));
                } else {
                    playfield.set(x, y, new BigPill(x, y));
                }
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
			System.err.println(playfield.toUnitTestString());
			System.out.println(playfield.compute());
        }
    }
}