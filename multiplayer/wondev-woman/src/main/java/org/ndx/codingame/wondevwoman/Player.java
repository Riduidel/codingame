package org.ndx.codingame.wondevwoman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.ndx.codingame.gaming.tounittest.ToUnitTestStringBuilder;
import org.ndx.codingame.wondevwoman.actions.Dual;
import org.ndx.codingame.wondevwoman.entities.Gamer;
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
			final List<String> rows = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				final String row = in.next();
				rows.add(row);
			}
			final Playfield playfield = Playfield.from(rows);
			final List<Gamer> my = readGamers(in, unitsPerPlayer);
			playfield.withMy(my);
			final List<Gamer> enemy = readGamers(in, unitsPerPlayer);
			playfield.withEnemy(enemy);
			final int legalActions = in.nextInt();
			final Collection<Dual> actions = new ArrayList<>();
			for (int i = 0; i < legalActions; i++) {
				final String atype = in.next();
				final int index = in.nextInt();
				final String dir1 = in.next();
				final String dir2 = in.next();
				actions.add(new Dual(atype, index, dir1, dir2));
			}
			playfield.withActions(actions);
			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");
			final String effectiveCommand = playfield.computeMoves();
			System.err.println(ToUnitTestStringBuilder.canComputeAt(playfield, effectiveCommand));
			System.out.println(effectiveCommand);
		}
	}

	private static List<Gamer> readGamers(final Scanner in, final int unitsPerPlayer) {
		final List<Gamer> my = new ArrayList<>();
		for (int i = 0; i < unitsPerPlayer; i++) {
			final int unitX = in.nextInt();
			final int unitY = in.nextInt();
			my.add(new Gamer(unitX, unitY, i));
		}
		return my;
	}
}