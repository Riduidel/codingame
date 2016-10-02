import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;

import org.junit.Ignore;
import org.junit.Test;

public class PlayerTest {
	public static class TestDelay extends Player.Delay {
		private int count, start;

		public TestDelay(int count) {
			super();
			this.start = this.count = count;
		}
		@Override
		public boolean isElapsed(long delay) {
			return count--<0;
		}
		@Override
		public long howLong() {
			return start-count;
		}
	}
	public static Player.Playground read(Collection<String> rows) {
		Player.Playground returned = null;
		int rowIndex = 0;
		for (String string : rows) {
			if(returned==null) {
				returned = new Player.Playground(string.length(), rows.size());
			}
			returned.readRow(string, rowIndex++);
		}
		return returned;
	}
	public static class PlaygroundTest {
		@Test public void can_read_playground() {
			Player.Playground read = read(Arrays.asList(
					".0", 
					".X", 
					"X."));
			assertThat(read.width).isEqualTo(2);
			assertThat(read.height).isEqualTo(3);
			assertThat(read.get(0, 0)).isInstanceOf(Player.Nothing.class);
			assertThat(read.get(1, 0)).isInstanceOf(Player.Box.class);
			assertThat(read.get(0, 2)).isInstanceOf(Player.Wall.class);
		}

		@Test public void can_write_playground_to_physical() {
			Player.Playground read = read(Arrays.asList(
					".0", 
					".X", 
					"X."));
			assertThat(read.toPhysicialString()).isEqualTo(
					".0\n"
					+ ".X\n"
					+ "X.");
		}

		@Test public void can_write_playground_with_game_infos_to_physical() {
			Player.Playground read = read(Arrays.asList(
					".0", 
					".X", 
					"X."));
			read.readGameEntities(
					new Player.Gamer(0, 0, 0, 1, 3),
					new Player.Bomb(0, 0, 1, 2, 3),
					new Player.Item(0, 1, 0, 1, 0)
					);
			assertThat(read.toString()).isEqualTo(
					"|G(1,3)| I(1) \n"+
					"|B(2,3)|  X   \n"+
					"|  X   |  .   ");
		}
	}
	
	public static class BombTest {
		@Test public void can_iterate_until_explosion() {
			Player.Playground read = read(Arrays.asList(
					".0.", 
					".X.", 
					"X.."));
			read.readGameEntities(new Player.Bomb(0, 0, 0, 2, 3));
			assertThat(read.get(0, 0)).isInstanceOf(Player.Bomb.class);
			read = read.next();
			assertThat(read.get(0, 0)).isInstanceOf(Player.Bomb.class);
			assertThat(read.get(0, 1)).isInstanceOf(Player.BombDanger.class);
			assertThat(((Player.Bomb) read.get(0, 0)).delay).isEqualTo(1);
			read = read.next();
			assertThat(read.get(0, 0)).isInstanceOf(Player.Fire.class);
			assertThat(read.get(1, 0)).isInstanceOf(Player.FireThenItem.class);
			assertThat(read.get(0, 1)).isInstanceOf(Player.Fire.class);
			assertThat(read.get(0, 1)).isNotInstanceOf(Player.FireThenItem.class);
			assertThat(read.get(1, 1)).isInstanceOf(Player.Wall.class);
			assertThat(read.get(0, 2)).isInstanceOf(Player.Wall.class);
		}
		
		@Test public void can_chain_explosions() {
			Player.Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			read.readGameEntities(
					new Player.Bomb(0, 0, 0, 2, 3),
					new Player.Bomb(0, 0, 2, 5, 3));
			assertThat(read.get(0, 0)).isInstanceOf(Player.Bomb.class);
			read = read.next();
			assertThat(read.get(0, 0)).isInstanceOf(Player.Bomb.class);
			assertThat(((Player.Bomb) read.get(0, 0)).delay).isEqualTo(1);
			assertThat(((Player.Bomb) read.get(0, 2)).delay).isEqualTo(4);
			read = read.next();
			assertThat(read.get(0, 0)).isInstanceOf(Player.Fire.class);
			assertThat(read.get(0, 1)).isInstanceOf(Player.Fire.class);
			assertThat(read.get(1, 0)).isInstanceOf(Player.Fire.class);
			assertThat(read.get(1, 1)).isInstanceOf(Player.Wall.class);
			assertThat(read.get(0, 2)).isInstanceOf(Player.Fire.class);
			assertThat(read.get(1, 2)).isInstanceOf(Player.Fire.class);
			assertThat(read.get(2, 2)).isInstanceOf(Player.Fire.class);
		}
		@Test public void can_chain_bombs_1475243223242() {
			Player.Delay delay = new Player.Delay();
			Player.Playground tested = read(Arrays.asList(
				"....",
				".X..",
				"..0.",
				"X..."
				));
			Player.Gamer me = new Player.Gamer(0, 0, 0, 1, 3);
			tested.readGameEntities(
				new Player.Bomb(0, 1, 0, 1, 3),
				new Player.Bomb(0, 2, 0, 2, 3),
				new Player.Bomb(0, 2, 1, 3, 3)
				);
			Player.Playground nextStep = tested.next();
			assertThat(nextStep.get(0, 0)).isInstanceOf(Player.Fire.class);
			assertThat(nextStep.get(1, 0)).isInstanceOf(Player.Fire.class);
			assertThat(nextStep.get(2, 0)).isInstanceOf(Player.Fire.class);
			assertThat(nextStep.get(3, 0)).isInstanceOf(Player.Fire.class);
			assertThat(nextStep.get(2, 1)).isInstanceOf(Player.Fire.class);
			assertThat(nextStep.get(3, 1)).isInstanceOf(Player.Fire.class);
			assertThat(nextStep.get(2, 2)).isInstanceOf(Player.Fire.class);
		}
	}
	public static class TrajectoryTest {
		public static class TestTrajectoryBuilder extends Player.TrajectoryBuilder {
			private static class StepElements {
				public final Player.Action action;
				public final int x;
				public final int y;
				public StepElements(Player.Action action, int x, int y) {
					super();
					this.action = action;
					this.x = x;
					this.y = y;
				}
			}
			private Deque<StepElements> futureSteps = new ArrayDeque<>();

			public TestTrajectoryBuilder(Player.Playground source, Player.Delay delay, Player.EvolvableConstants constants) {
				super(source, delay, constants);
			}

			public TestTrajectoryBuilder bomb(int i, int j) {
				futureSteps.add(new StepElements(Player.Action.BOMB, i, j));
				return this;
			}

			public TestTrajectoryBuilder move(int i, int j) {
				futureSteps.add(new StepElements(Player.Action.MOVE, i, j));
				return this;
			}
			@Override
			public Player.Step createStep(Player.Entity current, Player.Gamer me, Player.Playground playground, int time, int count) {
				if(futureSteps.isEmpty())
					return null;
				StepElements elements = futureSteps.removeFirst();
				return new Player.Step(elements.action, elements.x, elements.y, me, current).computeScore(playground, constants);
			}
			
		}
		private TestTrajectoryBuilder builder(Player.Playground read, Player.Delay delay, Player.EvolvableConstants evolvableConstants) {
			return new TestTrajectoryBuilder(read, delay, evolvableConstants);
		}
		@Test public void can_derive_a_move() {
			Player.Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Player.Gamer me = new Player.Gamer(0, 0, 0, 1, 3);
			Player.Step derivator = new Player.Step(Player.Action.MOVE, 0, 1, me, me);
			Player.Playground derived = read.next(derivator);
			assertThat(derived.get(0, 1)).isInstanceOf(Player.Nothing.class);
		}
		@Test public void can_derive_a_bomb() {
			Player.Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Player.Gamer me = new Player.Gamer(0, 0, 0, 1, 3);
			Player.Step derivator = new Player.Step(Player.Action.BOMB, 0, 1, me, me);
			Player.Playground derived = read.next(derivator);
			// bomb is dropped at current location
			assertThat(derived.get(0, 0))
				.isInstanceOf(Player.Bomb.class)
				// And bomb is due to explode in 8 turns, which means on supposed next turn it will be 7
				.extracting("delay").containsExactly(Player.EvolvableConstants.BOMB_DELAY-1);
		}
		@Test public void can_score_a_winning_trajectory() {
			Player.Delay delay = new TestDelay(10);
			Player.Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Player.Gamer me = new Player.Gamer(0, 0, 0, 1, 3);
			Player.Trajectory survival = builder(read, delay, new Player.EvolvableConstants())
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
			Player.Delay delay = new TestDelay(10);
			Player.Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Player.Gamer me = new Player.Gamer(0, 0, 0, 1, 3);
			Player.Trajectory noMove = builder(read, delay, new Player.EvolvableConstants())
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
//			Player.Delay delay = new TestDelay(10000000);
			Player.Delay delay = new Player.Delay();
			Player.Playground read = read(Arrays.asList(
					".0..", 
					".X..", 
					"..0.",
					"X..."));
			Player.Gamer me = new Player.Gamer(0, 0, 0, 1, 3);
			Player.Trajectory best = new Player.TrajectoryBuilder(read, delay, new Player.EvolvableConstants())
					.findBest(me);
			assertThat(best.score).isGreaterThan(0);
		}
	}
	
	public static class InGameTests {
		@Test public void can_find_move_1475247342954() {
//			Player.Delay delay = new TestDelay(1000000);
			Player.Delay delay = new Player.Delay();
			Player.Playground tested = read(Arrays.asList(
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
			Player.Gamer me = new Player.Gamer(0, 0, 0, 1, 3);
			tested.readGameEntities(
				new Player.Gamer(1, 12, 10, 1, 3),
				new Player.Bomb(0, 2, 0, 4, 3)
				);
			Player.Trajectory best = new Player.TrajectoryBuilder(tested, delay, new Player.EvolvableConstants())
					.findBest(me);
			assertThat(best.score).isGreaterThan(0);
		}
	}
}
