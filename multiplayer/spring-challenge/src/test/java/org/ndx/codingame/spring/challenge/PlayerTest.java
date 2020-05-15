package org.ndx.codingame.spring.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.lib2d.Geometry.at;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.spring.challenge.actions.MoveTo;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.actions.Speed;
import org.ndx.codingame.spring.challenge.actions.Switch;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.playground.Playfield;


public class PlayerTest {
	public static Playfield read(List<String> rows) {
		int height = rows.size();
		int width = rows.get(0).length();
		Playfield base = new Playfield(width, height);
		for(int index=0; index<rows.size(); index++) {
			base.readRow(rows.get(index), 
					index);
		}
		base.init();
		return base;
	}
	public static AbstractObjectAssert<?,?> isMoveTo(AbstractPac my_p0, Map<Pac, PacAction> actions) {
		return assertThat(actions.get(my_p0)).isInstanceOf(MoveTo.class)
			.extracting(action -> ((MoveTo) action).destination());
	}

	public static void isSpeed(AbstractPac my_p0, Map<Pac, PacAction> actions) {
		assertThat(actions.get(my_p0)).isInstanceOf(Speed.class);
	}

	public static AbstractObjectAssert<?,?> isSwitchTo(AbstractPac my_p0, Map<Pac, PacAction> actions) {
		return assertThat(actions.get(my_p0)).isInstanceOf(Switch.class)
			.extracting(action -> ((Switch) action).type);
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_action_at_turn_7_1589558367233() {
		Playfield tested = read(Arrays.asList(
			"###############################",
			"#   #.#?###???#?#???### # #???#",
			"# # #.#O###?###?###?### # #?#?#",
			"# #   #. . .........    # ..#?#",
			"# #.#?###?#?#?###?#?#.### #?#?#",
			"# #.#O??????#?#?#?#..     #?#?#",
			"###.#.###?###?#?#?### ###.#?###",
			"#??.#.??#???#?????#??.#??.#???#",
			"#?#.###?#?#?#?###?#?#.#?###?#?#",
			"#?#   ..#?#?????????#.#?????#?#",
			"### # #?#?#?#?###?#?#.#?#?#?###",
			"    # #???#?#?????#?#.??#?#. . ",
			"###############################"
			));
		Pac
			my_p2 = new Pac(5, 3, 2, true, Type.SCISSORS, 0, 4),
			my_p0 = new Pac(23, 2, 0, true, Type.PAPER, 0, 4),
			my_p1 = new Pac(5, 9, 1, true, Type.PAPER, 0, 4),
			my_p3 = new Pac(21, 6, 3, true, Type.ROCK, 0, 3);
		tested.readGameEntities(my_p2, my_p0, my_p1, my_p3);
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */25);
		isMoveTo(my_p1, actions).isEqualTo(at(6, 9));
		assertThat(actions).isNotEmpty();
	}
}
