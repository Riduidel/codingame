package org.ndx.codingame.spring.challenge;

public class EvolvableConstants {

	public static final boolean DEBUG_PREDICTOR = false;
	// This is the one to change to have longer paths
	public static final int HORIZON_FOR_RECURSIVE_PATH = 5;
	public static final int MAX_SPEED_TURNS = 5;
	public static final int MAX_ABILITY_COOLDOWN = 10;
	public static final int INTERNAL_SCORE_FOR_BIG_PILL = 1000;
	public static final int INTERNAL_SCORE_FOR_SMALL_PILL = 100;
	public static final int INTERNAL_SCORE_FOR_POTENTIAL_SMALL_PILL = 1;
//	public static final long DELAY_FOR_PREDICTION = 1000*60*60;
	public static final long DELAY_FOR_PREDICTION = 20;
	public static final int DISTANCE_UNREACHABLE = 1000;
	public static final int INTERNAL_SCORE_FOR_PAC = -1000;
	public static final int INTERNAL_SCORE_FOR_TEAMMATE_TOO_CLOSE = -1;
	public static final int INTERNAL_SCORE_FOR_TEAMMATE_TOO_FAR = 0;
	public static final int INTERNAL_SCORE_FOR_ENEMY_PREDATOR = -1000;
	public static final int INTERNAL_SCORE_FOR_NON_DANGEROUS = -10;
	public static final int INTERNAL_SCORE_FOR_ENEMY_PREY = 20;
	public static final int INTERNAL_SCORE_FOR_PAC_TRACE = 10;
	public static final double DISTANCE_ENEMY_TOO_CLOSE = 16;
}