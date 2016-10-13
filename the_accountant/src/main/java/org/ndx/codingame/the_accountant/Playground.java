package org.ndx.codingame.the_accountant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.lib2d.Point;
import org.ndx.codingame.lib2d.Segment;

public class Playground {

	private static final String DEFAULT_ACTION = "MOVE "+Agent.MAX_X/2+" "+Agent.MAX_Y/2;
	public final Collection<DataPoint> data;
	public final SortedSet<Enemy> enemies;
	private Playground derived;

	public Playground(Collection<DataPoint> data, SortedSet<Enemy> enemies) {
		this.data = data;
		this.enemies = enemies;
	}

	public String toString() {
		return toUnitTestString(null);
	}
	public String toUnitTestString(Agent agent) {
		StringBuilder returned = new StringBuilder();
		returned.append("\t@Test public void test_playground_at_").append(System.currentTimeMillis()).append("() {\n");
		if(agent!=null)
			returned.append("\t\tAgent agent = new Agent(").append(agent.x).append(", ").append(agent.y).append(");\n");
		returned.append("\t\tCollection<DataPoint> data = new ArrayList<DataPoint>();\n");
		for (DataPoint dataPoint : data) {
			returned.append("\t\tdata.add(new DataPoint(").append(dataPoint.id).append(", ").append(dataPoint.x).append(", ").append(dataPoint.y).append("));\n");
		}
		returned.append("\t\tSortedSet<Enemy> enemies = new TreeSet<Enemy>();\n");
		for (Enemy enemy : enemies) {
			returned.append("\t\tenemies.add(new Enemy(")
				.append(enemy.id).append(", ")
				.append(enemy.x).append(", ")
				.append(enemy.y).append(", ")
				.append(enemy.life).append(")\n")
				.append("\t\t\t.findTargetIn(data));\n");
		}
		returned.append("\t\tPlayground tested = new Playground(data, enemies);\n");
		returned.append("\t\tString strategy = tested.executeStrategy(agent, new Delay());\n");
		returned.append("\t\tensureValidityOf(agent, tested, strategy);\n");
		returned.append("\t}\n");
		return returned.toString();
	}
	
	public Playground derive() {
		if(derived==null) {
			derived = derive(data, enemies); 
		}
		return derived;
	}
	/**
	 * Derive this playground into next turn playground
	 * @return
	 */
	public static Playground derive(Collection<DataPoint> data, SortedSet<Enemy> enemies) {
		Collection<Point> eliminated = new ArrayList<>();
		Collection<Enemy> nextEnemies = new ArrayList<>();
		Collection<Enemy> toRefocus = new ArrayList<>();
		for (Enemy enemy : enemies) {
			Enemy nextRound = enemy.moveForward();
			if(enemy.target!=null) {
				// Now check if this enemy has eliminated target
				if(!eliminated.contains(enemy.target)) {
					if(new Segment(enemy, nextRound).contains(enemy.target)) {
						// Eliminate that point, and find mark enemy to find new target
						eliminated.add(enemy.target);
						toRefocus.add(nextRound);
					} else {
						nextRound.target = enemy.target;
						nextRound.distance = enemy.distance-Enemy.ENEMY_SPEED;
					}
					// In the other case, we only have to let things go on (enemy hasn't reached target yet)
				} else {
					// target was eliminated by another enemy
					toRefocus.add(nextRound);
				}
			}
			nextEnemies.add(nextRound);
		}
		List<DataPoint> remainingData = data.stream().filter((d)->!eliminated.contains(d)).collect(Collectors.toList());
		// Now, refocus enemies
		for (Enemy enemy : toRefocus) {
			enemy.findTargetIn(remainingData);
		}
		return new Playground(remainingData, new TreeSet<>(nextEnemies));
	}

	public String executeStrategy(Agent agent, Delay delay) {
		// maybe reorder enemies by putting first the ones we can one-shot
		List<Enemy> sortedEnemies = new ArrayList<>(enemies.size());
		List<Enemy> dangerous = new ArrayList<>();
		for (Enemy enemy : enemies) {
			if(agent.computeDamageTo(enemy)>=enemy.life) {
				dangerous.add(enemy);
			} else {
				sortedEnemies.add(enemy);
			}
		}
		sortedEnemies.addAll(0, dangerous);
		int score = Integer.MIN_VALUE;
		String action = DEFAULT_ACTION;
		for (Enemy enemy : sortedEnemies) {
			Strategy strategy = createStrategyFor(agent, enemy, this);
			if(strategy.score>score) {
				score = strategy.score;
				action = strategy.getStep();
			}
			if(delay.isElapsed(50))
				break;
		}
		return action;
	}

	private Strategy createStrategyFor(Agent agent, Enemy enemy, Playground playground) {
		return new Strategy(agent, enemy, playground, 0).computeOn();
	}

	public Enemy findEnemyById(Enemy enemy) {
		for (Enemy e : enemies) {
			if(e.id==enemy.id)
				return e;
		}
		return null;
	}
}
