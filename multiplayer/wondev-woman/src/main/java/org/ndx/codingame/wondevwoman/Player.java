package org.ndx.codingame.wondevwoman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.ndx.codingame.wondevwoman.actions.LegalAction;
import org.ndx.codingame.wondevwoman.entities.Floor;
import org.ndx.codingame.wondevwoman.entities.Gamer;
import org.ndx.codingame.wondevwoman.entities.Hole;
import org.ndx.codingame.wondevwoman.playground.Playfield;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
public class Player {

	public static void main(final String args[]) {
		final Scanner in = new Scanner(System.in);
		final int size = in.nextInt();
		final int unitsPerPlayer = in.nextInt();

		// game loop
		while (true) {
			Playfield playfield = null;
			final List<Gamer> my = new ArrayList<>();
			final List<Gamer> enemy = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				final String row = in.next();
				if (i == 0) {
					playfield = new Playfield(row.length(), size);
				}
				final byte[] bytes = row.getBytes();
				for (int j = 0; j < bytes.length; j++) {
					if (bytes[j] == '.') {
						playfield.set(j, i, Hole.instance);
					} else {
						playfield.set(j, i, new Floor());
					}
				}
			}
			for (int i = 0; i < unitsPerPlayer; i++) {
				final int unitX = in.nextInt();
				final int unitY = in.nextInt();
				my.add(new Gamer(unitX, unitY, i));
			}
			for (int i = 0; i < unitsPerPlayer; i++) {
				final int otherX = in.nextInt();
				final int otherY = in.nextInt();
				enemy.add(new Gamer(otherX, otherY, i));
			}
			final int legalActions = in.nextInt();
			final Collection<LegalAction> actions = new ArrayList<>();
			for (int i = 0; i < legalActions; i++) {
				final String atype = in.next();
				final int index = in.nextInt();
				final String dir1 = in.next();
				final String dir2 = in.next();
				actions.add(new LegalAction(atype, index, dir1, dir2));
			}
			System.err.println(actions);
			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");
			for (final Gamer g : my) {
				System.out.println(g.compute(playfield, enemy, actions));
			}
		}
	}
}