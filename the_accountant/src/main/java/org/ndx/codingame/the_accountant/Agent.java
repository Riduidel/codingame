package org.ndx.codingame.the_accountant;

import java.util.Collection;

import org.ndx.codingame.lib2d.Point;

public class Agent extends Point {

	public Agent(double x, double y) {
		super(x, y);
	}

	public void protectPointsFromEnemies(Collection<DataPoint> data, Collection<Enemy> enemies) {
		System.err.println(String.format("Should protect points \n%s\nfrom enemies\n%s", data, enemies));
	}

}
