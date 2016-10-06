package org.ndx.codingame.the_accountant;

import java.util.Collection;

import org.ndx.codingame.lib2d.Point;
import org.ndx.codingame.lib2d.Segment;

public class Agent extends Point {
	private Collection<DataPoint> toProtect;
	private Enemy target;
	private String command;

	public Agent(double x, double y) {
		super(x, y);
	}

	public void refineStrategy(Enemy enemy) {
		System.err.println(String.format("refining strategy to handle %s", enemy));
		if(command==null) {
			command = buildCommand(enemy);
		} else if(distance2To(enemy)<3000){
			System.err.println("Command is built, but this enemy is too close");
			command = buildCommand(enemy);
		} else {
			System.err.println("Command is built");
		}
	}

	private String buildCommand(Enemy enemy) {
		double distance2To = distance2To(enemy);
		if(distance2To>4000 && !enemy.target.equals(this)) {
			System.err.println(String.format("Too far (%f), approaching", distance2To));
			return String.format("MOVE %d %d", (int) enemy.x, (int) enemy.y);
		} else if(distance2To<3000) {
			System.err.println(String.format("Too close (%f), leaving", distance2To));
			Segment leadingToSelf = new Segment(enemy, this);
			Point target = leadingToSelf.pointAtNTimes(2);
			return String.format("MOVE %d %d", (int) target.x, (int) target.y);
		} else {
			System.err.println("At range, shooting");
			return String.format("SHOOT %d", enemy.id);
		}
	}

	public void protectingDataPoints(Collection<DataPoint> data) {
		this.toProtect = data;
	}

	public String executeStrategy() {
		return command;
	}

	@Override
	public String toString() {
		return "Agent [x=" + x + ", y=" + y + "]";
	}

}
