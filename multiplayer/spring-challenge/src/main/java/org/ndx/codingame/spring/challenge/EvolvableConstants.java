package org.ndx.codingame.spring.challenge;

public class EvolvableConstants {

	public static final int HORIZON_FOR_PACS = Integer.MAX_VALUE;
	public static final int HORIZON_FOR_BIG_PILLS = Integer.MAX_VALUE;
	public static final long COMPUTATION_DIRECTION = 20;
	// This is the one to change to have longer paths
	public static final int HORIZON_FOR_RANDOM_PATH = 10;
	public static final int MAX_SPEED_TURNS = 5;
	public static final int MAX_ABILITY_COOLDOWN = 10;
	public static final int INTERNAL_SCORE_FOR_SMALL_PILL = 2;
	public static final int INTERNAL_SCORE_FOR_POTENTIAL_SMALL_PILL = 1;
	public static final int INTERNAL_SCORE_FOR_BIG_PILL = 50;
//	public static final long DELAY_FOR_PREDICTION = 1000*60*60*10;
	public static final long DELAY_FOR_PREDICTION = 30;
	public static final int INTERNAL_SCORE_FOR_PAC = -1000;
	public static final int DISTANCE_UNREACHABLE = 1000;
	public static final int INTERNAL_SCORE_FOR_TEAMMATE_TOO_CLOSE = -500;
	public static final int INTERNAL_SCORE_FOR_TEAMMATE_TOO_FAR = 0;
	public static final int INTERNAL_SCORE_FOR_ENEMY_PREDATOR = -100;
	public static final int INTERNAL_SCORE_FOR_ENEMY_PREY = 100;
	public static final int MAX_POTENTIAL_PILLS = 5;
	public static final int INTERNAL_SCORE_FOR_PAC_TRACE = 100;
	public static final double DISTANCE_TEAMMATE_TOO_CLOSE = 9;
}