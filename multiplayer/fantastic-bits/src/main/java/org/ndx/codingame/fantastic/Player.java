package org.ndx.codingame.fantastic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.fantastic.status.TeamStatus;

/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle and use your team id to determine where you need to throw it.
 **/
public class Player {

    public static void main(final String args[]) {
        final Scanner in = new Scanner(System.in);
        final int myTeamId = in.nextInt(); // if 0 you need to score on the right of the map, if 1 you need to score on the left

        final List<Entity> playing = new ArrayList<>();
        final List<Wizard> myTeam = new ArrayList<>();
        final Status status = new Status();
        status.setTeam(myTeamId);
        status.setCaptain(0);
        // game loop
        while (true) {
        	playing.clear();
        	myTeam.clear();
            final int entities = in.nextInt(); // number of entities still in game
			for (int i = 0; i < entities; i++) {
                final int entityId = in.nextInt(); // entity identifier
                final String entityType = in.next(); // "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" (or "BLUDGER" after first league)
                final int x = in.nextInt(); // position
                final int y = in.nextInt(); // position
                final int vx = in.nextInt(); // velocity
                final int vy = in.nextInt(); // velocity
                final int state = in.nextInt(); // 1 if the wizard is holding a Snaffle, 0 otherwise
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
			chooseCaptain(status, myTeam);
            System.err.println(Playground.toUnitTestString(status, playing, myTeam));
    		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
    		final Collection<String> actions = entitiesStore.computeActionsToString();
    		for(final String s : actions) {
    			System.out.println(s);
    		}
            status.advanceOneTurn();
        }
    }

	private static void chooseCaptain(final Status status, final List<Wizard> myTeam) {
		final Wizard myCaptain = myTeam.get(0);
		final Wizard wingman = myTeam.get(1);
		if(myCaptain.isBetween(wingman, status.get(TeamStatus.class).getAttacked())) {
			myCaptain.setAttacking(true);
			status.setCaptain(myCaptain.id);
		} else {
			if(status.getCaptain()==myCaptain.id) {
				// we change captain only when really deserved
				if(Math.abs(myCaptain.position.x-wingman.position.x)<Constants.RADIUS_WIZARD*2) {
					myCaptain.setAttacking(true);
				}
			}
			wingman.setAttacking(true);
			status.setCaptain(wingman.id);
		}
	}
}