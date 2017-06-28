package org.ndx.codingame.wondevwoman.playground;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.wondevwoman.TestUtils.g;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.ndx.codingame.wondevwoman.actions.B;
import org.ndx.codingame.wondevwoman.actions.WonderAction;
import org.ndx.codingame.wondevwoman.entities.Gamer;

public class PlayfieldActionsComputerTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_correct_move_list_1() {
		final List<Gamer> my = Arrays.asList(
				g(3, 1, 0),
				g(5, 5, 1)
				);
		final List<Gamer> enemy = Arrays.asList(
				g(-1, -1, 1)
				);
		final List<WonderAction> actions = Arrays.asList(
				B.g(0).m("E").b("N"),
				B.g(0).m("E").b("NW"),
				B.g(0).m("E").b("S"),
				B.g(0).m("E").b("SE"),
				B.g(0).m("E").b("W"),
				B.g(0).m("N").b("E"),
				B.g(0).m("N").b("S"),
				B.g(0).m("N").b("SE"),
				B.g(0).m("N").b("SW"),
				B.g(0).m("N").b("W"),
				B.g(0).m("NW").b("E"),
				B.g(0).m("NW").b("S"),
				B.g(0).m("NW").b("SE"),
				B.g(0).m("NW").b("SW"),
				B.g(0).m("NW").b("W"),
				B.g(0).m("SE").b("E"),
				B.g(0).m("SE").b("N"),
				B.g(0).m("SE").b("NW"),
				B.g(0).m("SE").b("SE"),
				B.g(0).m("SE").b("SW"),
				B.g(0).m("W").b("E"),
				B.g(0).m("W").b("N"),
				B.g(0).m("W").b("NE"),
				B.g(0).m("W").b("NW"),
				B.g(0).m("W").b("SW"),
				B.g(0).m("W").b("W"),
				B.g(1).m("W").b("E"),
				B.g(1).m("W").b("N"),
				B.g(1).m("W").b("NE"),
				B.g(1).m("W").b("NW"),
				B.g(1).m("W").b("W")
				);
		final Gaming g = Gaming.from(
				"000034",
				"000014",
				"00..00",
				"0.10.0",
				"012022",
				"103000"
				);
		g.withMy(my)
		.withEnemy(enemy)
		.withActions(actions);
		assertThat(g.computeAvailableActions()).containsExactlyElementsOf(actions);
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_correct_move_list_2() {
		final List<Gamer> my = Arrays.asList(
				g(4, 1, 0),
				g(5, 5, 1)
				);
		final List<Gamer> enemy = Arrays.asList(
				g(-1, -1, 1)
				);
		final List<WonderAction> actions = Arrays.asList(
				B.g(0).m("NW").b("S"),
				B.g(0).m("NW").b("SE"),
				B.g(0).m("NW").b("SW"),
				B.g(0).m("NW").b("W"),
				B.g(0).m("S").b("E"),
				B.g(0).m("S").b("N"),
				B.g(0).m("S").b("NW"),
				B.g(0).m("S").b("SE"),
				B.g(0).m("S").b("SW"),
				B.g(0).m("SE").b("NW"),
				B.g(0).m("SE").b("S"),
				B.g(0).m("SE").b("W"),
				B.g(0).m("W").b("E"),
				B.g(0).m("W").b("N"),
				B.g(0).m("W").b("NW"),
				B.g(0).m("W").b("SE"),
				B.g(0).m("W").b("W"),
				B.g(1).m("W").b("E"),
				B.g(1).m("W").b("N"),
				B.g(1).m("W").b("NE"),
				B.g(1).m("W").b("NW"),
				B.g(1).m("W").b("W")
				);
		final Gaming g = Gaming.from(
				"000244",
				"000234",
				"00..00",
				"0.11.0",
				"042022",
				"223000"
				);
		g.withMy(my)
		.withEnemy(enemy)
		.withActions(actions);
		assertThat(g.computeAvailableActions()).containsExactlyElementsOf(actions);
	}
}
