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
	@Test public void can_find_action_at_turn_6_1589657817404() {
		Playfield tested = read(Arrays.asList(
			"###############################",
			"#####.????#   ###???#????.#####",
			"#####.##### #######?#####.#####",
			"........... ..#?#..?? .........",
			"###?#.###?# #?#?#.#?# ###.#?###",
			"????#.#???# ..... ..# ??#.#????",
			"#####?#?#?###?### ### #?# #####",
			"      #?#?????#?#     #?#      ",
			"### #.#?#####?#?#.#####?# #?###",
			"#   #..  . ...........    #???#",
			"# #.#####?#?#?#?#.#?#?#####?#?#",
			"# #.??????#???#?#.??#???????#?#",
			"#.#.#####?###?#?#.###?#####?#?#",
			"###############################"
			));
		Pac
			my_p4 = new Pac(17, 5, 4, true, Type.PAPER, 0, 4),
			my_p0 = new Pac(5, 7, 0, true, Type.ROCK, 1, 5),
			his_p1 = new Pac(27, 7, 1, false, Type.PAPER, 0, 4),
			my_p1 = new Pac(3, 7, 1, true, Type.DEAD, 0, 0),
			his_p2 = new Pac(1, 7, 2, false, Type.SCISSORS, 0, 4),
			his_p4 = new Pac(10, 9, 4, false, Type.PAPER, 0, 4),
			his_p0 = new Pac(24, 9, 0, false, Type.ROCK, 0, 4),
			my_p2 = new Pac(22, 9, 2, true, Type.SCISSORS, 4, 8),
			my_p3 = new Pac(11, 5, 3, true, Type.ROCK, 0, 0);
		SmallPill
			small_0 = new SmallPill(12, 5),
			small_1 = new SmallPill(16, 5),
			small_2 = new SmallPill(18, 5),
			small_3 = new SmallPill(17, 8),
			small_4 = new SmallPill(18, 9),
			small_5 = new SmallPill(17, 9),
			small_6 = new SmallPill(20, 9),
			small_7 = new SmallPill(19, 9),
			small_8 = new SmallPill(21, 9),
			small_9 = new SmallPill(14, 5),
			small_10 = new SmallPill(11, 9),
			small_11 = new SmallPill(17, 3),
			small_12 = new SmallPill(15, 9),
			small_13 = new SmallPill(17, 4),
			small_14 = new SmallPill(13, 9),
			small_15 = new SmallPill(6, 9),
			small_16 = new SmallPill(19, 5),
			small_17 = new SmallPill(5, 3),
			small_18 = new SmallPill(13, 5),
			small_19 = new SmallPill(15, 5),
			small_20 = new SmallPill(5, 2),
			small_21 = new SmallPill(5, 4),
			small_22 = new SmallPill(5, 5),
			small_23 = new SmallPill(12, 9),
			small_24 = new SmallPill(5, 1),
			small_25 = new SmallPill(14, 9),
			small_26 = new SmallPill(17, 11),
			small_27 = new SmallPill(5, 9),
			small_28 = new SmallPill(17, 12),
			small_29 = new SmallPill(16, 9),
			small_30 = new SmallPill(17, 10),
			small_31 = new SmallPill(5, 8);;
		tested.readGameEntities(my_p4, my_p0, his_p1, my_p1, his_p2, his_p4, his_p0, my_p2, my_p3, 
		small_0, small_1, small_2, small_3, small_4, small_5, small_6, small_7, small_8, small_9, small_10, small_11, small_12, small_13, small_14, small_15, small_16, small_17, small_18, small_19, small_20, small_21, small_22, small_23, small_24, small_25, small_26, small_27, small_28, small_29, small_30, small_31 );
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */-1);
		assertThat(actions.get(my_p3)).isNotInstanceOf(Switch.class);
		assertThat(actions).isNotEmpty();
	}
}
