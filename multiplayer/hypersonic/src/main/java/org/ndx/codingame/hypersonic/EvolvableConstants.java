package org.ndx.codingame.hypersonic;

public class EvolvableConstants {

	// Horizon should be long enough to cover most of the playground
	public static final int HORIZON = 20;
	public static final int BOMB_DELAY = 8;
	public static final int SCORE_CATCHED_ITEM = 2;
	public static final int SCORE_VISIT_BOX = 1;
	public static final int SCORE_EXPLODE_BOX = 5;
	public static final int SCORE_SUICIDE = -1000*HORIZON;
	public static final int SCORE_KILL_ENEMY = 10;
	public static final int SCORE_OUTSIDE = 0;
	public static final int SCORE_ON_TRACK = 0;
	public static final int SCORE_VISIT_WALL = 0;
	public static final int SCORE_VISIT_GAMER = -1;
	public static final int SCORE_VISIT_BOMB = -1;
	static final int OPPORTUNITY_BOMB = 1;
	static final int OPPORTUNITY_ITEM = 4;
}