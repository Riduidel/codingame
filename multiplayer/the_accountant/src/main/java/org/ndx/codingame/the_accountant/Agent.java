package org.ndx.codingame.the_accountant;

import java.util.ArrayList;
import java.util.Arrays;
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
	public static final int MIN_Y = 0;
	public static final int MIN_X = 0;
	public static final int MAX_Y = 9000;
	public static final int MAX_X = 16000;
	
	public Collection<Segment> BORDERS = Arrays.asList(
			Geometry.from(MIN_X, MIN_Y).segmentTo(MAX_X, MIN_Y),
			Geometry.from(MIN_X, MIN_Y).segmentTo(MIN_X, MAX_Y),
			Geometry.from(MAX_X, MIN_Y).segmentTo(MAX_X, MAX_Y),
			Geometry.from(MIN_X, MAX_Y).segmentTo(MAX_X, MAX_Y)
			);

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
	 * @param enemy
	 * @param enemies
	 * @return
	 */
	public Agent computeLocation(Playground playground, final Enemy enemy, Collection<Enemy> enemies) {
		Segment optimalDirection = new Segment(this, enemy);
		Collection<Enemy> dangerous = enemies.stream()
				.filter((e)->distance2To(e)<DANGER_ZONE)
				.collect(Collectors.toList());
		Agent initialAgent = optimalDirection.pointAtDistance(this, MAXIMUM_MOVE, this);
		if(dangerous.size()>0) {
			return computeLocationInDangerousSituation(dangerous);
		} else {
			return initialAgent;
		}
	}

	private Agent computeLocationInDangerousSituation(Collection<Enemy> dangerous) {
		Point barycenter = Geometry.barycenterOf(dangerous);
		Segment runAway = new Segment(barycenter, this);
		Agent finalAgent = runAway.pointAtDistance(barycenter, Enemy.ENEMY_SPEED+DEAD_ZONE, this);
		if(finalAgent.x<MIN_X||finalAgent.x>=MAX_X ||
				finalAgent.y<MIN_Y || finalAgent.y>=MAX_Y) {
			return computeLocationOnBorder(barycenter, dangerous, finalAgent);
		}
		return finalAgent;
	}

	private Agent computeLocationOnBorder(Point barycenter, Collection<Enemy> dangerous, Agent finalAgent) {
		Circle possible = new Circle(this, MAXIMUM_MOVE);
		Collection<Point> intersection = new ArrayList<Point>();
		for(Segment s : BORDERS) {
			intersection.addAll(possible.intersectionWith(s));
		}
		Point best = finalAgent;
		for (Point point : intersection) {
			if(best.minDistance2To(dangerous)<point.minDistance2To(dangerous)) {
				best = point;
			}
		}
		return build(best.x, best.y);
	}
}
