package org.ndx.codingame.fantastic.actions;

import java.util.Comparator;

public class OrderActions implements Comparator<Action> {

	@Override
	public int compare(final Action o1, final Action o2) {
		return (int) Math.signum(o2.getScore()-o1.getScore());
	}

}
