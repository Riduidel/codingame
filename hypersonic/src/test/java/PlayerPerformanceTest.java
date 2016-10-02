import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

public class PlayerPerformanceTest {
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
	public static class InGameTests {
		@Test public void can_find_move_1475247342954() {
			Player.Delay delay = new PlayerTest.TestDelay(1000000);
//			Player.Delay delay = new Player.Delay();
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
			int turn = 0;
			while(turn<100) {
				Player.Trajectory best = new Player.TrajectoryBuilder(tested, delay, new Player.EvolvableConstants())
						.findBest(me);
				Player.Step step = best.steps.get(0);
				assertThat(best.score).isGreaterThan(0);
				tested = tested.next();
				me = new Player.Gamer(0, step.x, step.y, 1, 3);
			}
		}
	}
}
