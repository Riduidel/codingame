package org.ndx.codingame.spring.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.lib2d.Geometry.at;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.Test;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.spring.challenge.actions.MoveTo;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.actions.Speed;
import org.ndx.codingame.spring.challenge.actions.Switch;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
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
	@Test public void can_find_action_at_turn_6_1589727848236() {
		Playfield tested = read(Arrays.asList(
			"###############################",
			"###O??###?#?#     #?#?###??O###",
			"###?#?###?#?# #.# #?#?###?#?###",
			"#???#???????# #.# #???????#???#",
			"#?#####?#?###.### ###?#?#####?#",
			"#..   ..#??.?.??? ????#?? ????#",
			"##### #?###.#.#?# #?###?#?#####",
			"#..   ..#     #?# ????#???????#",
			"#?# ###?#.### #?# ###?#?###?#?#",
			"#?# ..#??.#   #?# ??#???#???#?#",
			"#####?#?#.# ###?###?#?#?#?#####",
			"###############################"
			));
		Pac
			my_p1 = new Pac(3, 5, 1, true, Type.PAPER, 1, 5),
			my_p0 = new Pac(17, 3, 0, true, Type.ROCK, 1, 5),
			my_p2 = new Pac(9, 7, 2, true, Type.SCISSORS, 0, 4);
		BigPill
			big_0 = new BigPill(27, 1),
			big_1 = new BigPill(3, 1);
		SmallPill
			small_0 = new SmallPill(2, 5),
			small_1 = new SmallPill(6, 5),
			small_2 = new SmallPill(9, 8),
			small_3 = new SmallPill(9, 9),
			small_4 = new SmallPill(9, 10),
			small_5 = new SmallPill(1, 5),
			small_6 = new SmallPill(7, 5);;
		tested.readGameEntities(my_p1, my_p0, my_p2, 
		big_0, big_1, 
		small_0, small_1, small_2, small_3, small_4, small_5, small_6 );
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */219);
		isMoveTo(my_p2, actions).isEqualTo(at(9, 8));
		assertThat(actions).isNotEmpty();
	}

}
