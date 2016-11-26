package org.ndx.codingame.fantastic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle and use your team id to determine where you need to throw it.
 **/
public class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int myTeamId = in.nextInt(); // if 0 you need to score on the right of the map, if 1 you need to score on the left

        List<Entity> playing = new ArrayList<>();
        List<Wizard> myTeam = new ArrayList<>();
        // game loop
        while (true) {
        	playing.clear();
        	myTeam.clear();
            int entities = in.nextInt(); // number of entities still in game
			for (int i = 0; i < entities; i++) {
                int entityId = in.nextInt(); // entity identifier
                String entityType = in.next(); // "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" (or "BLUDGER" after first league)
                int x = in.nextInt(); // position
                int y = in.nextInt(); // position
                int vx = in.nextInt(); // velocity
                int vy = in.nextInt(); // velocity
                int state = in.nextInt(); // 1 if the wizard is holding a Snaffle, 0 otherwise
                switch(entityType) {
                case "WIZARD":
                	myTeam.add(new Wizard(entityId, x, y, vx, vy, myTeamId, state==1));
                	break;
                case "OPPONENT_WIZARD":
                	playing.add(new Wizard(entityId, x, y, vx, vy, 1-myTeamId, state==1));
                	break;
                case "SNAFFLE":
                	playing.add(new Snaffle(entityId, x, y, vx, vy));
                	break;
                case "BLUDGER":
                	playing.add(new Bludger(entityId, x, y, vx, vy));
                	break;
                }
            }
            System.err.println(Playground.toUnitTestString(playing, myTeam));
            for(Wizard player : myTeam) {


                // Edit this line to indicate the action for each wizard (0 ≤ thrust ≤ 150, 0 ≤ power ≤ 500)
                // i.e.: "MOVE x y thrust" or "THROW x y power"
            	System.out.println(player.play(playing));
            }
        }
    }
}