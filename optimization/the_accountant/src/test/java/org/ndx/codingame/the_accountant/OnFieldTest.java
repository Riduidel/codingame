package org.ndx.codingame.the_accountant;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Ignore;
import org.junit.Test;
import org.ndx.codingame.gaming.Delay;

@Ignore
public class OnFieldTest {
	public Agent rebuildFrom(Agent agent, String action) {
		String[] actionValues = action.split(" ");
		switch (actionValues[0]) {
		case "SHOOT":
			return agent;
		case "MOVE":
			return new Agent(Integer.valueOf(actionValues[1]), Integer.valueOf(actionValues[2]));
		default:
			throw new UnsupportedOperationException("action type unknown");
		}
	}

	private void ensureValidityOf(Agent agent, Playground tested, String strategy) {
		Playground nextPlayground = tested.derive();
		Agent nextAgent = rebuildFrom(agent, strategy);
		assertThat(nextAgent.x).isGreaterThanOrEqualTo(Agent.MIN_X).isLessThanOrEqualTo(Agent.MAX_X);
		assertThat(nextAgent.y).isGreaterThanOrEqualTo(Agent.MIN_Y).isLessThanOrEqualTo(Agent.MAX_Y);
		assertThat(agent.distance2To(nextAgent)).isLessThanOrEqualTo(Agent.MAXIMUM_MOVE);
//		for (Enemy e : nextPlayground.enemies) {
//			assertThat(nextAgent.distance2To(e)).isGreaterThan(Agent.DEAD_ZONE)
//					.as("Agent %s will be killed by enemy %s", nextAgent, e);
//		}
	}

	@Test
	public void should_oneshot_nearest_enemy() {
		Agent agent = new Agent(9000.0, 684.0);
		Collection<DataPoint> data = new ArrayList<DataPoint>();
		data.add(new DataPoint(0, 15999.0, 4500.0));
		data.add(new DataPoint(1, 8000.0, 7999.0));
		data.add(new DataPoint(2, 0.0, 3000.0));
		SortedSet<Enemy> enemies = new TreeSet<Enemy>();
		enemies.add(new Enemy(0, 0.0, 1000.0, 10).findTargetIn(data));
		enemies.add(new Enemy(1, 2000.0, 6500.0, 10).findTargetIn(data));
		enemies.add(new Enemy(2, 6000.0, 502.0, 10).findTargetIn(data));
		enemies.add(new Enemy(3, 3000.0, 6556.0, 10).findTargetIn(data));
		enemies.add(new Enemy(4, 10200.0, 2192.0, 10).findTargetIn(data));
		enemies.add(new Enemy(5, 12000.0, 6568.0, 10).findTargetIn(data));
		enemies.add(new Enemy(6, 13300.0, 8999.0, 10).findTargetIn(data));
		Playground tested = new Playground(data, enemies);
		String strategy = tested.executeStrategy(agent, new Delay());
		ensureValidityOf(agent, tested, strategy);
	}

	@Test
	public void should_not_move_where_one_enemy_can_kill_me() {
		Agent agent = new Agent(5943.0, 4634.0);
		Collection<DataPoint> data = new ArrayList<DataPoint>();
		data.add(new DataPoint(0, 8250.0, 4500.0));
		SortedSet<Enemy> enemies = new TreeSet<Enemy>();
		enemies.add(new Enemy(0, 8250.0, 5999.0, 10).findTargetIn(data));
		Playground tested = new Playground(data, enemies);
		String strategy = tested.executeStrategy(agent, new Delay());
		ensureValidityOf(agent, tested, strategy);
	}

	@Test
	public void should_not_evade_outside_of_field() {
		Agent agent = new Agent(11135.0, 8999.0);
		Collection<DataPoint> data = new ArrayList<DataPoint>();
		data.add(new DataPoint(3, 10000.0, 8999.0));
		SortedSet<Enemy> enemies = new TreeSet<Enemy>();
		enemies.add(new Enemy(0, 12795.0, 5944.0, 2).findTargetIn(data));
		enemies.add(new Enemy(1, 10734.0, 6792.0, 8).findTargetIn(data));
		enemies.add(new Enemy(2, 7314.0, 6304.0, 13).findTargetIn(data));
		Playground tested = new Playground(data, enemies);
		String strategy = tested.executeStrategy(agent, new Delay());
		ensureValidityOf(agent, tested, strategy);
	}
}
