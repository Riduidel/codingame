package org.ndx.codingame.the_accountant;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.ndx.codingame.lib2d.Circle;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.Point;
import org.ndx.codingame.lib2d.PointBuilder;
import org.ndx.codingame.lib2d.Segment;

public class Agent extends Point implements PointBuilder<Agent> {
	public static final double MAXIMUM_MOVE = 1000;
	public static final double DEAD_ZONE = 2000;
	
	private static final double DANGER_ZONE = MAXIMUM_MOVE+DEAD_ZONE;

	public Agent(double x, double y) {
		super(x, y);
	}
	
	public int computeDamageTo(Enemy enemy) {
		return computeDamageAt(distance2To(enemy));
	}

	public static int computeDamageAt(double distance2To) {
		return (int) Math.floor(125_000/Math.pow(distance2To, 1.2));
	}

	@Override
	public Agent build(double x, double y) {
		return new Agent(x, y);
	}

	public boolean endangeredBy(Enemy enemy) {
		return distance2To(enemy)<=DEAD_ZONE+MAXIMUM_MOVE;
	}

	/**
	 * 
	 * @param playground used <b>only</b> for size computation and evasion tactics
	 * @param target
	 * @param enemies
	 * @return
	 */
	public Agent computeLocation(Playground playground, final Point target, Collection<Enemy> enemies) {
		Segment optimalDirection = new Segment(this, target);
		Collection<Enemy> dangerous = enemies.stream()
				.filter((e)->distance2To(e)<DANGER_ZONE)
				.collect(Collectors.toList());
		Agent initialAgent = optimalDirection.pointAtDistance(this, MAXIMUM_MOVE, this);
		if(dangerous.size()>0) {
			Point barycenter = Geometry.barycenterOf(dangerous);
			Segment runAway = new Segment(barycenter, this);
			Agent finalAgent = runAway.pointAtDistance(barycenter, Enemy.ENEMY_SPEED+DEAD_ZONE, this);
			double nearest = 16000;
			for (Enemy enemy : dangerous) {
				if(enemy.distance2To(finalAgent)<nearest)
					nearest = enemy.distance2To(finalAgent);
			}
			if(finalAgent.x<0||finalAgent.x>=16000 ||
					finalAgent.y<0 || finalAgent.y>=9000) {
				double finalX,
					finalY;
				System.err.println("damn, can't go any further");
			}
			return finalAgent;
		} else {
			return initialAgent;
		}
	}
}
