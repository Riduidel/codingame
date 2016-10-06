package org.ndx.codingame.the_accountant;

import java.util.Collection;

import org.ndx.codingame.lib2d.Point;

public class Enemy extends Point implements Comparable<Enemy> {

	public final int id;
	public final int life;
	public double distance = Integer.MAX_VALUE;
	public Point target;

	public Enemy(int enemyId, int enemyX, int enemyY, int enemyLife) {
		super(enemyX, enemyY);
		this.id = enemyId;
		this.life = enemyLife;
	}

	/**
	 * Find this enemy target in avaiable datapoints
	 * @param data
	 */
	public void findTargetIn(Collection<DataPoint> data) {
		for (DataPoint dataPoint : data) {
			double distance2To = distance2To(dataPoint);
			if(distance2To<distance) {
				target = dataPoint;
				distance = distance2To;
			}
		}
	}

	@Override
	public int compareTo(Enemy o) {
		int returned = (int) (Math.signum(distance)-o.distance);
		if(returned==0)
			returned = (int) Math.signum(id-o.id);
		return id;
	}

	@Override
	public String toString() {
		return "Enemy [id=" + id + ", life=" + life + ", distance=" + distance + ", target=" + target + ", x=" + x
				+ ", y=" + y + "]";
	}

}
