package org.ndx.codingame.labyrinth;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;

public class PlayField extends Playground<Character> {

	public PlayField(int width, int height) {
		super(width, height);
	}

	public void setRow(int y, String row) {
		char[] c = row.toCharArray();
		for (int x = 0; x < c.length; x++) {
			DiscretePoint p = new DiscretePoint(x, y);
			set(p, c[x]);
		}
	}
}
