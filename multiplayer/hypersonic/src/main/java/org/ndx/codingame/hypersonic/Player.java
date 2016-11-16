package org.ndx.codingame.hypersonic;
import java.util.Scanner;

import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.hypersonic.content.Bomb;
import org.ndx.codingame.hypersonic.content.Item;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
public class Player {
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int width = in.nextInt();
		int height = in.nextInt();
		EvolvableConstants constants = new EvolvableConstants();
		Playfield playground = new Playfield(width, height);
		int myId = in.nextInt();
		in.nextLine();
		// game loop
		while (true) {
			playground.clear();
			Delay delay = new Delay();
			for (int rowIndex = 0; rowIndex < height; rowIndex++) {
				String row = in.nextLine();
				playground.readRow(row, rowIndex);
			}
			int entities = in.nextInt();
			Gamer me = null;
			for (int i = 0; i < entities; i++) {
				int entityType = in.nextInt();
				int owner = in.nextInt();
				int x = in.nextInt();
				int y = in.nextInt();
				int param1 = in.nextInt();
				int param2 = in.nextInt();
				switch (entityType) {
				case 0:
					// bomber
					if(owner==myId)
						me = new Gamer(owner, x, y, param1, param2);
					else
						playground.readGameEntities(new Gamer(owner, x, y, param1, param2));
					break;
				case 1:
					// bomb
					playground.readGameEntities(new Bomb(owner, x, y, param1, param2));
					break;
				case 2:
					// item
					playground.readGameEntities(new Item(owner, x, y, param1, param2));
					break;
				}
			}
			in.nextLine();

			System.err.println(playground.toUnitTestString(me));
			System.out.println(me.compute(playground));
		}
	}
}