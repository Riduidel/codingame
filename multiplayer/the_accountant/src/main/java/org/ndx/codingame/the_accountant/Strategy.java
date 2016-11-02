package org.ndx.codingame.the_accountant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.ndx.codingame.lib2d.Circle;
import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Strategy implements Comparable<Strategy> {
	private static final int HORIZON = 100;
	private static final int AGENT_DEAD = -1000;
	private static final int TARGET_MISSED = -100;
	private static final int ENEMY_KILLED = 100;

	public static enum Action {
		MOVE {

			public Collection<Strategy> createStrategiesFor(Strategy strategy, Playground playground) {
				Playground nextOne = playground.derive();
				Enemy nextEnemy = nextOne.findEnemyById(strategy.enemy);
				if(nextEnemy==null) {
					return Arrays.asList();
				}
				Collection<Enemy> enemies = new ArrayList<>(playground.enemies.size()*2);
//				enemies.addAll(playground.enemies);
				enemies.addAll(nextOne.enemies);
				Agent nextAgent = strategy.agent.computeLocation(nextOne, nextEnemy, enemies);
				Strategy returned = new Strategy(
						nextAgent, 
						nextEnemy,
						nextOne, strategy.depth+1);
				if(nextEnemy.target==strategy.enemy.target) {
					// Nothing has changed
				} else {
					// All those moves were useless, continuing the search, but with bad score set
					returned.score = TARGET_MISSED;
				}
				return Arrays.asList(returned);
			}

			@Override
			public String getStep(Strategy strategy) {
				ContinuousPoint destination = null;
				Playground nextOne = strategy.playground.derive();
				Enemy nextEnemy = nextOne.findEnemyById(strategy.enemy);
				if(nextEnemy==null) {
					destination = strategy.agent;
				} else {
					Collection<Enemy> enemies = new ArrayList<>(strategy.playground.enemies.size()*2);
//					enemies.addAll(strategy.playground.enemies);
					enemies.addAll(nextOne.enemies);
					destination = strategy.agent.computeLocation(nextOne, nextEnemy, nextOne.enemies);
				}
				// Move directly in enemy direction
				return String.format("MOVE %d %d", (int) destination.x, (int) destination.y);
			}
			
		},
		SHOOT {

			public Collection<Strategy> createStrategiesFor(Strategy strategy, Playground playground) {
				Enemy target = strategy.enemy;
				SortedSet<Enemy> filtered = new TreeSet<>();
				for(Enemy e : playground.enemies) {
					if(e.id!=target.id) {
						filtered.add(e);
					}
				}
				int damage = strategy.agent.computeDamageTo(target);
				// Now consider enemy shot
				Enemy hit = new Enemy(target.id, target.x, target.y, target.life-damage);
				Strategy next = new Strategy(strategy.agent, hit, 
						Playground.derive(playground.data, filtered), strategy.depth+1);
				next.score = damage-1;
				if(hit.life>0) {
					hit.findTargetIn(playground.data);
					filtered.add(hit);
				} else {
					// kill bonus
					next.score=ENEMY_KILLED;
				}
				// Now alter score according to survival of agent
				for (Enemy enemy : filtered) {
					if(next.agent.distance2To(enemy)<Agent.DEAD_ZONE) {
						next.score = AGENT_DEAD;
					}
				}
				return Arrays.asList(next);
			}

			@Override
			public String getStep(Strategy firstKey) {
				// Move directly in enemy direction
				return String.format("SHOOT %d", firstKey.enemy.id);
			}
			
		};

		public abstract Collection<Strategy> createStrategiesFor(Strategy strategy, Playground playground);

		public abstract String getStep(Strategy firstKey);
	}
	
	Agent agent;
	Enemy enemy;
	public final int depth;
	public final SortedMap<Strategy, Action> strategies = new TreeMap<>();
	public int score;
	public final Playground playground;

	public Strategy(Agent agent, Enemy enemy, Playground playground, int depth) {
		this.agent = agent;
		this.enemy = enemy;
		this.playground = playground;
		this.depth = depth;
	}

	public String getStep() {
		Strategy firstKey = strategies.firstKey();
		Action strategyType = strategies.get(firstKey);
		System.err.println("Applying strategy "+strategyType.name());
		return strategyType.getStep(firstKey);
	}

	public Strategy computeOn() {
		if(playground.data.isEmpty()) {
			// game is lost
			score = AGENT_DEAD;
			return this;
		} else if(playground.enemies.isEmpty()) {
			// All enemies are dead !
			return this;
		} else if(depth>HORIZON) {
			return this;
		} else {
			for(Action action : Action.values()) {
				Collection<Strategy> possible = action.createStrategiesFor(this, playground);
				for(Strategy s : possible) {
					s.computeOn();
					strategies.put(s, action);
				}
			}
			score += strategies.firstKey().score-1;
			return this;
		}
	}

	@Override
	public int compareTo(Strategy o) {
		if(this==o)
			return 0;
		int returned = 0;
		if(returned==0) {
			returned = (int) Math.signum(o.score-score);
		}
		if(returned==0) {
			returned = (int) Math.signum(hashCode()-o.hashCode());
		}
		return returned;
	}

	public String toString() {
		return toString("").toString();
	}

	private StringBuilder toString(String string) {
		StringBuilder returned = new StringBuilder();
		for(Map.Entry<Strategy, Action> entry : strategies.entrySet()) {
			Strategy strategy = entry.getKey();
			returned.append("\n")
				.append(string).append(entry.getValue().getStep(strategy))
				.append(" (").append(strategy.score).append(")");
			returned.append(strategy.toString(string+"\t"));
		}
		return returned;
	}
}
