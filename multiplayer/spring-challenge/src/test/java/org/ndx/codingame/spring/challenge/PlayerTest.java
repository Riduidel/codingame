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
			.extracting(action -> ((MoveTo) action).destination);
	}

	public static void isSpeed(AbstractPac my_p0, Map<Pac, PacAction> actions) {
		assertThat(actions.get(my_p0)).isInstanceOf(Speed.class);
	}

	public static AbstractObjectAssert<?,?> isSwitchTo(AbstractPac my_p0, Map<Pac, PacAction> actions) {
		return assertThat(actions.get(my_p0)).isInstanceOf(Switch.class)
			.extracting(action -> ((Switch) action).type);
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Disabled @Test public void can_find_action_at_turn_0_1589484796745() {
		Playfield tested = read(Arrays.asList(
			"###################################",
			"###?#?#???#?#??.#?#???#?#.. #?#?###",
			"###?#?#?#?#?#?#.#?#?#?#?#?#.#?#?###",
			"??????#?#?????#.????#?????#.#??????",
			"###?###?#####?#.###?#?#####.###?###",
			"??????????????#.###?#??????.?.?????",
			"#####?#?###?###.###?###?###.#.#####",
			"..... #???#.... ... ....#??.# .....",
			"#####?###?#?###.###?###?#?###.#####",
			"??????#?????#??.??????#?????#.?????",
			"#####?#?###?#?#######?#?###?#.#####",
			"# ......###?#?????????#?###??.????#",
			"#.#?#?#?###?#O#######O#?###?#.#?#?#",
			"#.#???#?????????????????????#.??#?#",
			"###?#####?###?###?###?###?#####?###",
			"###???#??O??#???#?#???#??O??#???###",
			"###################################"
			));
		Pac
			my_p0 = new Pac(15, 7, 0, true, Type.ROCK, 0, 0),
			my_p3 = new Pac(27, 1, 3, true, Type.ROCK, 0, 0),
			my_p1 = new Pac(1, 11, 1, true, Type.PAPER, 0, 0),
			his_p2 = new Pac(5, 7, 2, false, Type.SCISSORS, 0, 0),
			my_p2 = new Pac(29, 7, 2, true, Type.SCISSORS, 0, 0),
			his_p0 = new Pac(19, 7, 0, false, Type.ROCK, 0, 0);
		tested.readGameEntities(my_p0, my_p3, my_p1, his_p2, my_p2, his_p0);
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */ 56);
		// Pac should go into dead end
		isMoveTo(my_p1, actions).isEqualTo(at(2, 11));
		assertThat(actions).isNotEmpty();
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Disabled @Test public void can_find_action_at_turn_14_1589526879811() {
		Playfield tested = read(Arrays.asList(
			"###################################",
			"###?#?#???# #   #?#m. # #   #?# ###",
			"###?#?#?#?#m# # #?#.#.# # #.#?# ###",
			" .m. .#?#..   # ... # .   #.#.. . .",
			"###?###?#####.# ###.#.#####.### ###",
			"....... ......# ###.#........   ...",
			"#####?#?###?### ###.###?###.# #####",
			"..... #???#.... ... ....#??.# .....",
			"#####?###?#?###.### ###?#?###.#####",
			"??????#?????#??.??? ??#?????#.?????",
			"#####?#?###?#?#######?#?###?#.#####",
			"#     ..###?#?????????#?###??.????#",
			"#.#.#?#?###?# #######O#?###?#.#?#?#",
			"#.#.??#?????????????????????#.??#?#",
			"###.#####?###?###?###?###?#####?###",
			"###.??#?? ??#???#?#???#??O??#???###",
			"###################################"
			));
		Pac
			my_p0 = new Pac(11, 1, 0, true, Type.ROCK, 0, 0),
			my_p2 = new Pac(4, 3, 2, true, Type.SCISSORS, 0, 4),
			my_p3 = new Pac(19, 3, 3, true, Type.ROCK, 0, 4),
			my_p1 = new Pac(5, 11, 1, true, Type.DEAD, 0, 0);
		tested.readGameEntities(my_p0, my_p2, my_p3, my_p1);
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */ 46);
		isMoveTo(my_p3, actions).isNotEqualTo(at(19, 2));
		assertThat(actions).isNotEmpty();
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Disabled @Test public void can_find_action_at_turn_13_1589528447503() {
		Playfield tested = read(Arrays.asList(
			"###################################",
			"###?#?#???#?#??.#?#???#?#.. #?#?###",
			"###?#?#?#?#?#?#.#?#?#?#?#?# #?#?###",
			"??????#?#?????#.????#?????# #??????",
			"###?###?#####?#.###?#?##### ###?###",
			"..... . ......#.###?#..     .......",
			"#####m#?###.###.###?### ###.#.#####",
			"      #???#     ...  m  #??.#      ",
			"##### ###?# ###.###?###.#?###.#####",
			"????? #.... # ?.??????#.????#.?????",
			"##### #.### # #######?#.###?#.#####",
			"# .    e### # ????????#.###??.????#",
			"# #.#.# ### #e#######O#.###?#.#?#?#",
			"# # . #m ..    ....... e    #.??#?#",
			"###.#####?###.###?###?###?#####?###",
			"###.??#??O??#.??#?#???#??O??#???###",
			"###################################"
			));
		Pac
			my_p1 = new Pac(8, 13, 1, true, Type.PAPER, 0, 0),
			my_p0 = new Pac(14, 13, 0, true, Type.ROCK, 0, 0),
			my_p2 = new Pac(5, 5, 2, true, Type.SCISSORS, 0, 0),
			my_p3 = new Pac(20, 7, 3, true, Type.ROCK, 0, 0),
			his_p1 = new Pac(22, 13, 1, false, Type.PAPER, 0, 0),
			his_p0 = new Pac(13, 13, 0, false, Type.PAPER, 0, 0);
		tested.readGameEntities(my_p1, my_p0, my_p2, my_p3, his_p1, his_p0);
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */87);
		isSwitchTo(my_p0, actions).isEqualTo(Type.SCISSORS);
		assertThat(actions).isNotEmpty();
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Disabled @Test public void can_find_action_at_turn_21_1589532163378() {
		Playfield tested = read(Arrays.asList(
			"###################################",
			"###?#?#???#?#??.#?#???#?#.. #?#?###",
			"###?#?#?#?#?#?#.#?#?#?#?#?# #?#?###",
			"??????#?#?????#.????#?????# #??????",
			"###?###?#####?#.###?#?##### ###?###",
			"....... ......#.###?#.......  .....",
			"#####?#?###?###.###?###?###.# #####",
			"..... #???#.... ... ....#??.# .....",
			"#####?###?#?### ###?###?#?###.#####",
			"??????#?????#   ..  ..#?????#.?????",
			"#####?#?###?# #######?#?###?#.#####",
			"#     ..###?#         #?###??.????#",
			"# #?#?#?###?# ####### #?###?#.#?#?#",
			"# #???#??????.??????????????#.??#?#",
			"###?#####?###.###?###?###?#####?###",
			"###???#?? ??#.??#?#???#??O??#???###",
			"###################################"
			));
		Pac
			my_p3 = new Pac(27, 4, 3, true, Type.ROCK, 0, 0),
			my_p1 = new Pac(3, 11, 1, true, Type.DEAD, 0, 9),
			my_p0 = new Pac(21, 11, 0, true, Type.DEAD, 0, 9),
			my_p2 = new Pac(28, 5, 2, true, Type.SCISSORS, 0, 0);
		tested.readGameEntities(my_p3, my_p1, my_p0, my_p2);
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */25);
		assertThat(actions).isNotEmpty();
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Disabled @Test public void can_find_action_at_turn_2_1589548302915() {
		Playfield tested = read(Arrays.asList(
			"#################################",
			"#####???#???#?#?#?#?#???#???#####",
			"#####?#?#?#?#?#?#?#?#?#?#?#?#####",
			"# .m..#???#?????#?????#???#?????#",
			"#.# #?###?#?#?#####?#?#?###?#?#?#",
			"#O# #.. ..#???????????#?????#?#O#",
			"### ###m#?###?#?#?#?###?#?###?###",
			"...m#.. #?????#???#?????#??.#e...",
			"###.#?#.###?#?#?#?#?#?###?#.#?###",
			"???.??#.????#?#???#?#?????#.?????",
			"#####?###?#?#?#####?#?#?###.#####",
			"??????#???#???????????#???#.?????",
			"#####?#?#?###?#####?###?#?#.#####",
			"....e. O#?????#####?????#O m ....",
			"#################################"
			));
		Pac
			my_p1 = new Pac(3, 5, 1, true, Type.PAPER, 5, 9),
			his_p4 = new Pac(6, 13, 4, false, Type.PAPER, 4, 8),
			my_p3 = new Pac(7, 7, 3, true, Type.ROCK, 0, 0),
			my_p2 = new Pac(3, 6, 2, true, Type.ROCK, 0, 8),
			my_p0 = new Pac(1, 3, 0, true, Type.ROCK, 4, 8),
			my_p4 = new Pac(26, 13, 4, true, Type.PAPER, 0, 0);
		tested.readGameEntities(my_p1, his_p4, my_p3, my_p2, my_p0, my_p4);
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */1024);
		assertThat(actions).isNotEmpty();
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_action_at_turn_1_1589548649175() {
		Playfield tested = read(Arrays.asList(
			"#################################",
			"#####???#???#?#?#?#?#???#???#####",
			"#####?#?#?#?#?#?#?#?#?#?#?#?#####",
			"#.. ..#???#?????#?????#???#?????#",
			"#?#m#?###?#?#?#####?#?#?###?#?#?#",
			"#O# #..m..#???????????#?????#?#O#",
			"###.### #?###?#?#?#?###?#?###?###",
			"... #??.#?????#???#?????#??.# ...",
			"###.#?#.###?#?#?#?#?#?###?#.#?###",
			"???.??#.????#?#???#?#?????#.?????",
			"#####?###?#?#?#####?#?#?###.#####",
			"??????#???#???????????#???#.?????",
			"#####?#?#?###?#####?###?#?#.#####",
			".... ..O#?????#####?????#O. m....",
			"#################################"
			));
		Pac
			my_p1 = new Pac(3, 5, 1, true, Type.PAPER, 0, 0),
			my_p0 = new Pac(3, 3, 0, true, Type.ROCK, 5, 9),
			my_p2 = new Pac(3, 7, 2, true, Type.ROCK, 0, 9),
			my_p3 = new Pac(7, 6, 3, true, Type.ROCK, 0, 0),
			his_p4 = new Pac(4, 13, 4, false, Type.PAPER, 5, 9),
			his_p2 = new Pac(29, 7, 2, false, Type.SCISSORS, 5, 9),
			my_p4 = new Pac(27, 13, 4, true, Type.PAPER, 0, 0);
		tested.readGameEntities(my_p1, my_p0, my_p2, my_p3, his_p4, his_p2, my_p4);
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */-1);
		// This one should be pulled by the small pill below
//		isMoveTo(my_p1, actions).isEqualTo(at(3, 6));
		// THis one should be pulled by the long corridor, but there is the dead end bonus, and I'm sure it's the source of the problem
		isMoveTo(my_p2, actions).isEqualTo(at(2, 7));
		assertThat(actions).isNotEmpty();
	}	
}
