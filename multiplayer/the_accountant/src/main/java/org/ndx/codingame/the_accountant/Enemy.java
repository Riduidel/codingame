package org.ndx.codingame.the_accountant;

import java.util.Collection;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;

public class Enemy extends ContinuousPoint implements Comparable<Enemy> {

	public static final int ENEMY_SPEED = 500;
	public final int id;
	public final int life;
	public double distance = Integer.MAX_VALUE;
	public ContinuousPoint target;

	public Enemy(final int enemyId, final double enemyX, final double enemyY, final int enemyLife) {
		super(enemyX, enemyY);
		id = enemyId;
		life = enemyLife;
	}

	/**
	 * Find this enemy target in avaiable datapoints
	 * @param data
	 */
	public Enemy findTargetIn(final Collection<DataPoint> data) {
		for (final DataPoint dataPoint : data) {
			final double distance2To = distance2To(dataPoint);
			if(distance2To<distance) {
				target = dataPoint;
				distance = distance2To;
			}
		}
		return this;
	}

	@Override
	public int compareTo(final Enemy o) {
		int returned = (int) (Math.signum(distance)-o.distance);
		if(returned==0) {
			returned = (int) Math.signum(id-o.id);
		}
		return id;
	}

	@Override
	public String toString() {
		return "Enemy [id=" + id + ", life=" + life + ", distance=" + distance + ", target=" + target + ", x=" + x
				+ ", y=" + y + "]";
	}

	public Enemy selectTargetBetween(final Agent agent, final Collection<DataPoint> data) {
        if(data.isEmpty()) {
        	target = agent;
        	distance = distance2To(agent);
        } else {
        	findTargetIn(data);
        }
        return this;
	}

	public int getTurnsToReachTarget() {
		return (int) distance/ENEMY_SPEED;
	}

	/**
	 * Create a new enemy, mvoed forward of 500 points
	 * @return
	 */
	public Enemy moveForward() {
		if(target==null) {
			return this;
		}
		final Segment segment = new Segment(this, target);
		return (Enemy) segment.pointAtDistance(this, ENEMY_SPEED, this);
	}

	@Override
	public Enemy build(final double x, final double y) {
		return new Enemy(id, x, y, life);
	}

}
