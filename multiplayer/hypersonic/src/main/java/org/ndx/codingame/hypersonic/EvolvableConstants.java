package org.ndx.codingame.hypersonic;

public class EvolvableConstants {

	public static final int HORIZON = 16;
	public static final int BOMB_DELAY = 8;
	public int COUNT_ENOUGH_TRAJECTORIES = 1000;
	public static final int ITERATIVE_TIME_LIMIT = 3;
	public static final int ADAPATION_FACTOR = 10;
	public Integer SCORE_NO_EVASION_MALUS = -1;
	public Integer SCORE_ON_THE_ROAD = 4;
	public Integer SCORE_ENDANGERED = -100;
	public Integer SCORE_BAD_MOVE = -200;
	public Integer SCORE_CATCHED_ITEM = 20;
	public Integer SCORE_START_BOMB_CHAIN = 5;
	public Integer SCORE_EXPLODE_BOX = 50;
	public Integer SCORE_SUICIDE = -10000;
	public Integer SCORE_KILL_ENEMY = 10;
	public Integer SCORE_BURNT_ITEM = -5;
	public int SCORE_DROP_BOMB_TAX = -10;
	public final long DELAY_CREATE_TRAJECTORIES = 80;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (DELAY_CREATE_TRAJECTORIES ^ (DELAY_CREATE_TRAJECTORIES >>> 32));
		result = prime * result + ((SCORE_BAD_MOVE == null) ? 0 : SCORE_BAD_MOVE.hashCode());
		result = prime * result + ((SCORE_BURNT_ITEM == null) ? 0 : SCORE_BURNT_ITEM.hashCode());
		result = prime * result + ((SCORE_CATCHED_ITEM == null) ? 0 : SCORE_CATCHED_ITEM.hashCode());
		result = prime * result + SCORE_DROP_BOMB_TAX;
		result = prime * result + ((SCORE_ENDANGERED == null) ? 0 : SCORE_ENDANGERED.hashCode());
		result = prime * result + ((SCORE_EXPLODE_BOX == null) ? 0 : SCORE_EXPLODE_BOX.hashCode());
		result = prime * result + ((SCORE_KILL_ENEMY == null) ? 0 : SCORE_KILL_ENEMY.hashCode());
		result = prime * result + ((SCORE_NO_EVASION_MALUS == null) ? 0 : SCORE_NO_EVASION_MALUS.hashCode());
		result = prime * result + ((SCORE_ON_THE_ROAD == null) ? 0 : SCORE_ON_THE_ROAD.hashCode());
		result = prime * result + ((SCORE_START_BOMB_CHAIN == null) ? 0 : SCORE_START_BOMB_CHAIN.hashCode());
		result = prime * result + ((SCORE_SUICIDE == null) ? 0 : SCORE_SUICIDE.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EvolvableConstants other = (EvolvableConstants) obj;
		if (DELAY_CREATE_TRAJECTORIES != other.DELAY_CREATE_TRAJECTORIES)
			return false;
		if (SCORE_BAD_MOVE == null) {
			if (other.SCORE_BAD_MOVE != null)
				return false;
		} else if (!SCORE_BAD_MOVE.equals(other.SCORE_BAD_MOVE))
			return false;
		if (SCORE_BURNT_ITEM == null) {
			if (other.SCORE_BURNT_ITEM != null)
				return false;
		} else if (!SCORE_BURNT_ITEM.equals(other.SCORE_BURNT_ITEM))
			return false;
		if (SCORE_CATCHED_ITEM == null) {
			if (other.SCORE_CATCHED_ITEM != null)
				return false;
		} else if (!SCORE_CATCHED_ITEM.equals(other.SCORE_CATCHED_ITEM))
			return false;
		if (SCORE_DROP_BOMB_TAX != other.SCORE_DROP_BOMB_TAX)
			return false;
		if (SCORE_ENDANGERED == null) {
			if (other.SCORE_ENDANGERED != null)
				return false;
		} else if (!SCORE_ENDANGERED.equals(other.SCORE_ENDANGERED))
			return false;
		if (SCORE_EXPLODE_BOX == null) {
			if (other.SCORE_EXPLODE_BOX != null)
				return false;
		} else if (!SCORE_EXPLODE_BOX.equals(other.SCORE_EXPLODE_BOX))
			return false;
		if (SCORE_KILL_ENEMY == null) {
			if (other.SCORE_KILL_ENEMY != null)
				return false;
		} else if (!SCORE_KILL_ENEMY.equals(other.SCORE_KILL_ENEMY))
			return false;
		if (SCORE_NO_EVASION_MALUS == null) {
			if (other.SCORE_NO_EVASION_MALUS != null)
				return false;
		} else if (!SCORE_NO_EVASION_MALUS.equals(other.SCORE_NO_EVASION_MALUS))
			return false;
		if (SCORE_ON_THE_ROAD == null) {
			if (other.SCORE_ON_THE_ROAD != null)
				return false;
		} else if (!SCORE_ON_THE_ROAD.equals(other.SCORE_ON_THE_ROAD))
			return false;
		if (SCORE_START_BOMB_CHAIN == null) {
			if (other.SCORE_START_BOMB_CHAIN != null)
				return false;
		} else if (!SCORE_START_BOMB_CHAIN.equals(other.SCORE_START_BOMB_CHAIN))
			return false;
		if (SCORE_SUICIDE == null) {
			if (other.SCORE_SUICIDE != null)
				return false;
		} else if (!SCORE_SUICIDE.equals(other.SCORE_SUICIDE))
			return false;
		return true;
	}
	
}