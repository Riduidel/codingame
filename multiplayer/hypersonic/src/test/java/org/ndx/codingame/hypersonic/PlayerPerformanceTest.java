package org.ndx.codingame.hypersonic;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.ndx.codingame.gaming.Delay;

public class PlayerPerformanceTest {
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
	public static class InGameTests {
		@Test public void can_find_move_1475247342954() {
			Delay delay = new TestDelay(1000000);
//			Delay delay = new Delay();
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
			int turn = 0;
			while(turn<100) {
				Trajectory best = new TrajectoryBuilder(tested, delay, new EvolvableConstants())
						.findBest(me);
				Step step = best.steps.get(0);
				assertThat(best.score).isGreaterThan(0);
				tested = tested.next();
				me = new Gamer(0, step.x, step.y, 1, 3);
			}
		}
	}
}
