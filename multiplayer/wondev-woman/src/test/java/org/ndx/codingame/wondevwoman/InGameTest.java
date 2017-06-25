package org.ndx.codingame.wondevwoman;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.ndx.codingame.wondevwoman.actions.Dual;
import org.ndx.codingame.wondevwoman.entities.Gamer;
import org.ndx.codingame.wondevwoman.playground.Playfield;

public class InGameTest {

	private Gamer g(final int i, final int j, final int k) {
		return new Gamer(i, j, k);
	}

	private Dual d(final String string, final int i, final String string2, final String string3) {
		return new Dual(string,i, string2, string3);
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void just_to_always_keep_assertj_in_imports() {
		assertThat(true).isNotEqualTo(false);
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_at__1498411732665() {
		final List<Gamer> my = Arrays.asList(
				g(2, 5, 0),
				g(3, 3, 1)
				);
		final List<Gamer> enemy = Arrays.asList(
				g(2, 3, 0),
				g(3, 4, 1)
				);
		final List<Dual> actions = Arrays.asList(
				d("MOVE&BUILD", 0, "E", "E"),
				d("MOVE&BUILD", 0, "E", "NE"),
				d("MOVE&BUILD", 0, "E", "W"),
				d("MOVE&BUILD", 0, "NW", "N"),
				d("MOVE&BUILD", 0, "NW", "NW"),
				d("MOVE&BUILD", 0, "NW", "S"),
				d("MOVE&BUILD", 0, "NW", "SE"),
				d("MOVE&BUILD", 0, "NW", "SW"),
				d("MOVE&BUILD", 0, "NW", "W"),
				d("MOVE&BUILD", 0, "W", "E"),
				d("MOVE&BUILD", 0, "W", "N"),
				d("MOVE&BUILD", 0, "W", "NW"),
				d("MOVE&BUILD", 0, "W", "W"),
				d("MOVE&BUILD", 1, "E", "E"),
				d("MOVE&BUILD", 1, "E", "N"),
				d("MOVE&BUILD", 1, "E", "NE"),
				d("MOVE&BUILD", 1, "E", "NW"),
				d("MOVE&BUILD", 1, "E", "S"),
				d("MOVE&BUILD", 1, "E", "SE"),
				d("MOVE&BUILD", 1, "E", "W"),
				d("MOVE&BUILD", 1, "N", "E"),
				d("MOVE&BUILD", 1, "N", "N"),
				d("MOVE&BUILD", 1, "N", "NE"),
				d("MOVE&BUILD", 1, "N", "NW"),
				d("MOVE&BUILD", 1, "N", "S"),
				d("MOVE&BUILD", 1, "N", "SE"),
				d("MOVE&BUILD", 1, "N", "W"),
				d("MOVE&BUILD", 1, "NE", "E"),
				d("MOVE&BUILD", 1, "NE", "N"),
				d("MOVE&BUILD", 1, "NE", "NW"),
				d("MOVE&BUILD", 1, "NE", "S"),
				d("MOVE&BUILD", 1, "NE", "SE"),
				d("MOVE&BUILD", 1, "NE", "SW"),
				d("MOVE&BUILD", 1, "NE", "W"),
				d("MOVE&BUILD", 1, "NW", "E"),
				d("MOVE&BUILD", 1, "NW", "N"),
				d("MOVE&BUILD", 1, "NW", "NE"),
				d("MOVE&BUILD", 1, "NW", "NW"),
				d("MOVE&BUILD", 1, "NW", "SE"),
				d("MOVE&BUILD", 1, "NW", "SW"),
				d("MOVE&BUILD", 1, "NW", "W"),
				d("MOVE&BUILD", 1, "SE", "E"),
				d("MOVE&BUILD", 1, "SE", "N"),
				d("MOVE&BUILD", 1, "SE", "NE"),
				d("MOVE&BUILD", 1, "SE", "NW"),
				d("MOVE&BUILD", 1, "SE", "S"),
				d("MOVE&BUILD", 1, "SE", "SE"),
				d("MOVE&BUILD", 1, "SE", "SW"),
				d("PUSH&BUILD", 0, "NE", "E"),
				d("PUSH&BUILD", 0, "NE", "NE"),
				d("PUSH&BUILD", 1, "S", "S"),
				d("PUSH&BUILD", 1, "S", "SE"),
				d("PUSH&BUILD", 1, "W", "NW"),
				d("PUSH&BUILD", 1, "W", "SW"),
				d("PUSH&BUILD", 1, "W", "W")
				);
		final Playfield p = Playfield.from(
				"0.00.0",
				".0000.",
				"001100",
				"013210",
				"004010",
				"000000"
				);
		p.withMy(my)
		.withEnemy(enemy)
		.withActions(actions);
		assertThat(p.computeMoves()).startsWith("PUSH&BUILD 1 W");
	}
}
