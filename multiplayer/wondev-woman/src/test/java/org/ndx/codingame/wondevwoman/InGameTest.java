package org.ndx.codingame.wondevwoman;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.ndx.codingame.wondevwoman.actions.Dual;
import org.ndx.codingame.wondevwoman.entities.Gamer;
import org.ndx.codingame.wondevwoman.playground.Playfield;

public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void just_to_always_keep_assertj_in_imports() {
		assertThat(true).isNotEqualTo(false);
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_at__1498323098067() {
		final List<Gamer> my = Arrays.asList(
				new Gamer(1, 3, 0)
				);
		final List<Gamer> enemy = Arrays.asList(
				new Gamer(4, 5, 0)
				);
		final List<Dual> actions = Arrays.asList(
				new Dual("MOVE&BUILD", 0, "E", "E"),
				new Dual("MOVE&BUILD", 0, "E", "N"),
				new Dual("MOVE&BUILD", 0, "E", "NE"),
				new Dual("MOVE&BUILD", 0, "E", "NW"),
				new Dual("MOVE&BUILD", 0, "E", "S"),
				new Dual("MOVE&BUILD", 0, "E", "SE"),
				new Dual("MOVE&BUILD", 0, "E", "SW"),
				new Dual("MOVE&BUILD", 0, "E", "W"),
				new Dual("MOVE&BUILD", 0, "N", "E"),
				new Dual("MOVE&BUILD", 0, "N", "NE"),
				new Dual("MOVE&BUILD", 0, "N", "S"),
				new Dual("MOVE&BUILD", 0, "N", "SE"),
				new Dual("MOVE&BUILD", 0, "N", "SW"),
				new Dual("MOVE&BUILD", 0, "NE", "E"),
				new Dual("MOVE&BUILD", 0, "NE", "N"),
				new Dual("MOVE&BUILD", 0, "NE", "NE"),
				new Dual("MOVE&BUILD", 0, "NE", "S"),
				new Dual("MOVE&BUILD", 0, "NE", "SE"),
				new Dual("MOVE&BUILD", 0, "NE", "SW"),
				new Dual("MOVE&BUILD", 0, "NE", "W"),
				new Dual("MOVE&BUILD", 0, "S", "E"),
				new Dual("MOVE&BUILD", 0, "S", "N"),
				new Dual("MOVE&BUILD", 0, "S", "NE"),
				new Dual("MOVE&BUILD", 0, "S", "NW"),
				new Dual("MOVE&BUILD", 0, "S", "SE"),
				new Dual("MOVE&BUILD", 0, "SE", "E"),
				new Dual("MOVE&BUILD", 0, "SE", "N"),
				new Dual("MOVE&BUILD", 0, "SE", "NE"),
				new Dual("MOVE&BUILD", 0, "SE", "NW"),
				new Dual("MOVE&BUILD", 0, "SE", "S"),
				new Dual("MOVE&BUILD", 0, "SE", "SE"),
				new Dual("MOVE&BUILD", 0, "SE", "W"),
				new Dual("MOVE&BUILD", 0, "W", "E"),
				new Dual("MOVE&BUILD", 0, "W", "NE"),
				new Dual("MOVE&BUILD", 0, "W", "SE")
				);
		final Playfield p = Playfield.from(
				"...0...",
				"..000..",
				".00000.",
				"0010000",
				".00010.",
				"..000..",
				"...0..."
				);
		p.withMy(my)
		.withEnemy(enemy)
		.withActions(actions);
		assertThat(p.computeMoves()).startsWith("MOVE&BUILD 0 E");
	}

}
