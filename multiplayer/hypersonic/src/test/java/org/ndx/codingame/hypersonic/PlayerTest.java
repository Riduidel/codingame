package org.ndx.codingame.hypersonic;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;

import org.junit.Test;
import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.hypersonic.Action;
import org.ndx.codingame.hypersonic.Bomb;
import org.ndx.codingame.hypersonic.BombDanger;
import org.ndx.codingame.hypersonic.Box;
import org.ndx.codingame.hypersonic.Entity;
import org.ndx.codingame.hypersonic.EvolvableConstants;
import org.ndx.codingame.hypersonic.Fire;
import org.ndx.codingame.hypersonic.FireThenItem;
import org.ndx.codingame.hypersonic.Gamer;
import org.ndx.codingame.hypersonic.Item;
import org.ndx.codingame.hypersonic.Nothing;
import org.ndx.codingame.hypersonic.Playground;
import org.ndx.codingame.hypersonic.Step;
import org.ndx.codingame.hypersonic.Trajectory;
import org.ndx.codingame.hypersonic.TrajectoryBuilder;
import org.ndx.codingame.hypersonic.Wall;

public class PlayerTest {
	public static Playground read(Collection<String> rows) {
		Playground returned = null;
		int rowIndex = 0;
		for (String string : rows) {
			if(returned==null) {
				returned = new Playground(string.length(), rows.size());
			}
			returned.readRow(string, rowIndex++);
		}
		return returned;
	}
	public static class PlaygroundTest {
		@Test public void can_read_playground() {
			Playground read = read(Arrays.asList(
					".0", 
					".X", 
					"X."));
			assertThat(read.width).isEqualTo(2);
			assertThat(read.height).isEqualTo(3);
			assertThat(read.get(0, 0)).isInstanceOf(Nothing.class);
			assertThat(read.get(1, 0)).isInstanceOf(Box.class);
			assertThat(read.get(0, 2)).isInstanceOf(Wall.class);
		}

		@Test public void can_write_playground_to_physical() {
			Playground read = read(Arrays.asList(
					".0", 
					".X", 
					"X."));
			assertThat(read.toPhysicialString()).isEqualTo(
					".0\n"
					+ ".X\n"
					+ "X.");
		}

		@Test public void can_write_playground_with_game_infos_to_physical() {
			Playground read = read(Arrays.asList(
					".0", 
					".X", 
					"X."));
			read.readGameEntities(
					new Gamer(0, 0, 0, 1, 3),
					new Bomb(0, 0, 1, 2, 3),
					new Item(0, 1, 0, 1, 0)
					);
			assertThat(read.toString()).isEqualTo(
					"|G(1,3)| I(1) \n"+
					"|B(2,3)|  X   \n"+
					"|  X   |  .   ");
		}
	}
	
	public static class BombTest {
		@Test public void can_iterate_until_explosion() {
			Playground read = read(Arrays.asList(
					".0.", 
					".X.", 
					"X.."));
			read.readGameEntities(new Bomb(0, 0, 0, 2, 3));
			assertThat(read.get(0, 0)).isInstanceOf(Bomb.class);
			read = read.next();
			assertThat(read.get(0, 0)).isInstanceOf(Bomb.class);
			assertThat(read.get(0, 1)).isInstanceOf(BombDanger.class);
			assertThat(((Bomb) read.get(0, 0)).delay).isEqualTo(1);
			read = read.next();
			assertThat(read.get(0, 0)).isInstanceOf(Fire.class);
			assertThat(read.get(1, 0)).isInstanceOf(FireThenItem.class);
			assertThat(read.get(0, 1)).isInstanceOf(Fire.class);
			assertThat(read.get(0, 1)).isNotInstanceOf(FireThenItem.class);
			assertThat(read.get(1, 1)).isInstanceOf(Wall.class);
			assertThat(read.get(0, 2)).isInstanceOf(Wall.class);
		}
		
		@Test public void can_chain_explosions() {
			Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			read.readGameEntities(
					new Bomb(0, 0, 0, 2, 3),
					new Bomb(0, 0, 2, 5, 3));
			assertThat(read.get(0, 0)).isInstanceOf(Bomb.class);
			read = read.next();
			assertThat(read.get(0, 0)).isInstanceOf(Bomb.class);
			assertThat(((Bomb) read.get(0, 0)).delay).isEqualTo(1);
			assertThat(((Bomb) read.get(0, 2)).delay).isEqualTo(4);
			read = read.next();
			assertThat(read.get(0, 0)).isInstanceOf(Fire.class);
			assertThat(read.get(0, 1)).isInstanceOf(Fire.class);
			assertThat(read.get(1, 0)).isInstanceOf(Fire.class);
			assertThat(read.get(1, 1)).isInstanceOf(Wall.class);
			assertThat(read.get(0, 2)).isInstanceOf(Fire.class);
			assertThat(read.get(1, 2)).isInstanceOf(Fire.class);
			assertThat(read.get(2, 2)).isInstanceOf(Fire.class);
		}
		@Test public void can_chain_bombs_1475243223242() {
			Delay delay = new Delay();
			Playground tested = read(Arrays.asList(
				"....",
				".X..",
				"..0.",
				"X..."
				));
			Gamer me = new Gamer(0, 0, 0, 1, 3);
			tested.readGameEntities(
				new Bomb(0, 1, 0, 1, 3),
				new Bomb(0, 2, 0, 2, 3),
				new Bomb(0, 2, 1, 3, 3)
				);
			Playground nextStep = tested.next();
			assertThat(nextStep.get(0, 0)).isInstanceOf(Fire.class);
			assertThat(nextStep.get(1, 0)).isInstanceOf(Fire.class);
			assertThat(nextStep.get(2, 0)).isInstanceOf(Fire.class);
			assertThat(nextStep.get(3, 0)).isInstanceOf(Fire.class);
			assertThat(nextStep.get(2, 1)).isInstanceOf(Fire.class);
			assertThat(nextStep.get(3, 1)).isInstanceOf(Fire.class);
			assertThat(nextStep.get(2, 2)).isInstanceOf(Fire.class);
		}
	}
	public static class TrajectoryTest {
		public static class TestTrajectoryBuilder extends TrajectoryBuilder {
			private static class StepElements {
				public final Action action;
				public final int x;
				public final int y;
				public StepElements(Action action, int x, int y) {
					super();
					this.action = action;
					this.x = x;
					this.y = y;
				}
			}
			private Deque<StepElements> futureSteps = new ArrayDeque<>();

			public TestTrajectoryBuilder(Playground source, Delay delay, EvolvableConstants constants) {
				super(source, delay, constants);
			}

			public TestTrajectoryBuilder bomb(int i, int j) {
				futureSteps.add(new StepElements(Action.BOMB, i, j));
				return this;
			}

			public TestTrajectoryBuilder move(int i, int j) {
				futureSteps.add(new StepElements(Action.MOVE, i, j));
				return this;
			}
			@Override
			public Step createStep(Entity current, Gamer me, Playground playground, int time, int count) {
				if(futureSteps.isEmpty())
					return null;
				StepElements elements = futureSteps.removeFirst();
				return new Step(elements.action, elements.x, elements.y, me, current).computeScore(playground, constants);
			}
			
		}
		public static TestTrajectoryBuilder builder(Playground read, Delay delay, EvolvableConstants evolvableConstants) {
			return new TestTrajectoryBuilder(read, delay, evolvableConstants);
		}
		@Test public void can_derive_a_move() {
			Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Gamer me = new Gamer(0, 0, 0, 1, 3);
			Step derivator = new Step(Action.MOVE, 0, 1, me, me);
			Playground derived = read.next(derivator);
			assertThat(derived.get(0, 1)).isInstanceOf(Nothing.class);
		}
		@Test public void can_derive_a_bomb() {
			Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Gamer me = new Gamer(0, 0, 0, 1, 3);
			Step derivator = new Step(Action.BOMB, 0, 1, me, me);
			Playground derived = read.next(derivator);
			// bomb is dropped at current location
			assertThat(derived.get(0, 0))
				.isInstanceOf(Bomb.class)
				// And bomb is due to explode in 8 turns, which means on supposed next turn it will be 7
				.extracting("delay").containsExactly(EvolvableConstants.BOMB_DELAY-1);
		}
		@Test public void can_score_a_winning_trajectory() {
			Delay delay = new TestDelay(10);
			Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Gamer me = new Gamer(0, 0, 0, 1, 3);
			Trajectory survival = builder(read, delay, new EvolvableConstants())
					.move(0, 1)
					.move(0, 2)
					.move(1, 2)
					.bomb(0, 2)
					.move(0, 1)
					.move(0, 1)
					.move(0, 1)
					.build(me, 0);
			// We will earn some points here
			assertThat(survival.score).isGreaterThan(0);
		}
		@Test public void can_score_a__survival_trajectory() {
			Delay delay = new TestDelay(10);
			Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Gamer me = new Gamer(0, 0, 0, 1, 3);
			Trajectory noMove = builder(read, delay, new EvolvableConstants())
					.move(0, 1)
					.move(0, 1)
					.move(0, 1)
					.move(0, 1)
					.move(0, 1)
					.move(0, 1)
					.move(0, 1)
					.build(me, 0);
			// We will not loose, but neither win
			assertThat(noMove.score).isGreaterThanOrEqualTo(0);
		}
		@Test public void can_score_a_bunch_of_trajectories() {
//			Delay delay = new TestDelay(10000000);
			Delay delay = new Delay();
			Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Gamer me = new Gamer(0, 0, 0, 1, 3);
			Trajectory best = new TrajectoryBuilder(read, delay, new EvolvableConstants())
					.findBest(me);
			assertThat(best.score).isGreaterThan(0);
		}
	}
	
	public static class InGameTests {
		@Test public void can_find_move_1475247342954() {
//			Delay delay = new TestDelay(1000000);
			Delay delay = new Delay();
			Playground tested = read(Arrays.asList(
				"....00.00....",
				".X.X0X0X0X.X.",
				"000000.000000",
				"0X.X0X.X0X.X0",
				"0.00..0..00.0",
				".X0X0X0X0X0X.",
				"0.00..0..00.0",
				"0X.X0X.X0X.X0",
				"000000.000000",
				".X.X0X0X0X.X.",
				"....00.00...."
				));
			Gamer me = new Gamer(0, 0, 0, 1, 3);
			tested.readGameEntities(
				new Gamer(1, 12, 10, 1, 3),
				new Bomb(0, 2, 0, 4, 3)
				);
			Trajectory best = new TrajectoryBuilder(tested, delay, new EvolvableConstants())
					.findBest(me);
			assertThat(best.score).isGreaterThan(0);
		}
	}
}
