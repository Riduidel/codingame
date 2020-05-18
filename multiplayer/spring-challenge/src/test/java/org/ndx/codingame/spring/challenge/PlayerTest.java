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
	@Test public void can_find_action_at_turn_15_1589729250319() {
		Playfield tested = read(Arrays.asList(
			"###################################",
			"###?#???#???#?#?????#?#???#??.#.###",
			"###?#?#?#?#?#?###?###?#?#?#?#.#.###",
			"#?????#???#???#?????#???#???#     #",
			"# ###?###?###?#?###?#?###?### ### #",
			"#???#?#???#?????????????#???# #   #",
			"###?#?#?#?#?#?#######?#?#?#?# # ###",
			".....   #???#?????????#???#..   ...",
			"#####?#?###?#####?#####?###?# #####",
			"#?????#.              ...   #    .#",
			"#?###?### ###?#?#?#.# ###?###.###?#",
			"#?#???#.. ###???#?#.?.###???#.??#?#",
			"#?#?#?#?# ###?#######.###?#?#.#?#?#",
			"#?#?#???# ......###??.????#??.#?#?#",
			"###################################"
			));
		Pac
			my_p0 = new Pac(21, 10, 0, true, Type.ROCK, 0, 0),
			my_p1 = new Pac(29, 7, 1, true, Type.PAPER, 2, 6);
		SmallPill
			small_0 = new SmallPill(21, 11),
			small_1 = new SmallPill(29, 12),
			small_2 = new SmallPill(29, 13),
			small_3 = new SmallPill(29, 11),
			small_4 = new SmallPill(29, 10),
			small_5 = new SmallPill(1, 7),
			small_6 = new SmallPill(33, 7),
			small_7 = new SmallPill(0, 7),
			small_8 = new SmallPill(29, 1),
			small_9 = new SmallPill(2, 7),
			small_10 = new SmallPill(34, 7),
			small_11 = new SmallPill(29, 2),
			small_12 = new SmallPill(21, 12),
			small_13 = new SmallPill(21, 13);;
		tested.readGameEntities(my_p0, my_p1, 
		small_0, small_1, small_2, small_3, small_4, small_5, small_6, small_7, small_8, small_9, small_10, small_11, small_12, small_13 );
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */2115);
		isMoveTo(my_p0, actions).isEqualTo(at(21, 11));
		assertThat(actions).isNotEmpty();
	}
}
