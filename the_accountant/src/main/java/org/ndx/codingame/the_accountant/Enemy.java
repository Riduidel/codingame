package org.ndx.codingame.the_accountant;

import org.ndx.codingame.lib2d.Point;

public class Enemy extends Point {

	public final int id;
	public final int life;

	public Enemy(int enemyId, int enemyX, int enemyY, int enemyLife) {
		super(enemyX, enemyY);
		this.id = enemyId;
		this.life = enemyLife;
	}

}
