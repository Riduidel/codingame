package org.ndx.codingame.gameofdrones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import org.ndx.codingame.gameofdrones.entities.Drone;
import org.ndx.codingame.gameofdrones.entities.Zone;
import org.ndx.codingame.gameofdrones.playground.Playfield;

public class Player {
	public static void main(final String args[]) {
		final Scanner in = new Scanner(System.in);
		final int PLAYERS = in.nextInt(); // number of players in the game (2 to 4 players)
		final int ID = in.nextInt(); // ID of your player (0, 1, 2, or 3)
		final int DRONES = in.nextInt(); // number of drones in each team (3 to 11)
		final int ZONES = in.nextInt(); // number of zones on the map (4 to 8)
		final List<Zone> zones = new ArrayList<>();
		for (int i = 0; i < ZONES; i++) {
			final int X = in.nextInt(); // corresponds to the position of the center of a zone. A zone is a circle with a radius of 100 units.
			final int Y = in.nextInt();
			zones.add(new Zone(X, Y));
		}

		Map<Object, Map<Object, Optional<Drone>>> previousTurn = Collections.emptyMap();

		// game loop
		while (true) {
			final Playfield playfield = new Playfield();
			playfield.setOwner(ID);
			for (int i = 0; i < ZONES; i++) {
				final int TID = in.nextInt(); // ID of the team controlling the zone (0, 1, 2, or 3) or -1 if it is not controlled. The zones are given in the same order as in the initialization.
				playfield.addZone(zones.get(i).ownedBy(TID));
			}
			for (int i = 0; i < PLAYERS; i++) {
				for (int j = 0; j < DRONES; j++) {
					final int DX = in.nextInt(); // The first D lines contain the coordinates of drones of a player with the ID 0, the following D lines those of the drones of player 1, and thus it continues until the last player.
					final int DY = in.nextInt();
					playfield.addDrone(new Drone(DX, DY, i, j).mapToPrevious(previousTurn));
				}
			}
			final String effectiveCommand = playfield.computeMoves();
			System.err.println(playfield.toUnitTestString(effectiveCommand));
			System.out.println(effectiveCommand);
			previousTurn = playfield.dronesToArray();
		}
	}
}