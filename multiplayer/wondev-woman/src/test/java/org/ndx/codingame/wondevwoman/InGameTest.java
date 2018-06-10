package org.ndx.codingame.wondevwoman;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.wondevwoman.TestUtils.g;

import java.util.Arrays;
import java.util.List;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ndx.codingame.wondevwoman.actions.B;
import org.ndx.codingame.wondevwoman.actions.WonderAction;
import org.ndx.codingame.wondevwoman.entities.Gamer;
import org.ndx.codingame.wondevwoman.playground.Gaming;

public class InGameTest {
	private static final int PERCENTILE = 60;
	private static final int THREAD_COUNT = 1;
	private static final int INVOCATION_COUNT = 1;

	@BeforeClass
	public static void waitForVisualVM() {
		if(Boolean.parseBoolean(System.getProperty("PROFILING", "false"))) {
			System.out.println("Waiting for profiler");
			final long start = System.currentTimeMillis();
			while(System.currentTimeMillis()-start<60_000) {
				try {
					Thread.sleep(100);
				} catch (final InterruptedException e) {
				}
			}
			System.out.println("Starting now");
		}
	}

	@Test
	public void just_to_always_keep_assertj_in_imports() {
		assertThat(true).isNotEqualTo(false);
	}
	@PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_at__1498478452261() {
		final List<Gamer> my = Arrays.asList(
				g(5, 3, 0),
				g(4, 4, 1)
				);
		final List<Gamer> enemy = Arrays.asList(
				g(5, 4, 0),
				g(-1, -1, 1)
				);
		final List<WonderAction> actions = Arrays.asList(
				B.g(0).m("N").b("N"),
				B.g(0).m("N").b("NW"),
				B.g(0).m("N").b("S"),
				B.g(0).m("N").b("W"),
				B.g(0).m("NW").b("E"),
				B.g(0).m("NW").b("N"),
				B.g(0).m("NW").b("NE"),
				B.g(0).m("NW").b("NW"),
				B.g(0).m("NW").b("SE"),
				B.g(0).m("NW").b("SW"),
				B.g(1).m("NW").b("NE"),
				B.g(1).m("NW").b("S"),
				B.g(1).m("NW").b("SE"),
				B.g(1).m("NW").b("SW"),
				B.g(1).m("NW").b("W"),
				B.g(1).m("S").b("E"),
				B.g(1).m("S").b("N"),
				B.g(1).m("S").b("NW"),
				B.g(1).m("S").b("W"),
				B.g(1).m("SE").b("NW"),
				B.g(1).m("SE").b("W"),
				B.g(1).m("SW").b("E"),
				B.g(1).m("SW").b("N"),
				B.g(1).m("SW").b("NE"),
				B.g(1).m("SW").b("NW"),
				B.g(1).m("SW").b("W"),
				B.g(1).m("W").b("E"),
				B.g(1).m("W").b("N"),
				B.g(1).m("W").b("NW"),
				B.g(1).m("W").b("S"),
				B.g(1).m("W").b("SE"),
				B.g(1).m("W").b("SW"),
				B.g(1).m("W").b("W"),
				B.g(0).p("S").b("S"),
				B.g(0).p("S").b("SW")
				);
		final Gaming g = Gaming.from(
				"000000",
				"000000",
				"00..00",
				"0.00.0",
				"000000",
				"000000"
				);
		g.withMy(my)
		.withEnemy(enemy)
		.withActions(actions);
		assertThat(g.computeMoves()).isNotEqualTo("MOVE&BUILD 0 N N");
	}
}
