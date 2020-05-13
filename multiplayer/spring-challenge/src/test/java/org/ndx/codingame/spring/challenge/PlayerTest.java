package org.ndx.codingame.spring.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.lib2d.Geometry.at;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.Test;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.actions.MoveTo;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.playground.Playfield;


class PlayerTest {
	public static Playfield read(List<String> rows) {
		int height = rows.size();
		int width = rows.get(0).length();
		Playfield base = new Playfield(width, height);
		for(int index=0; index<rows.size(); index++) {
			base.readRow(rows.get(index), 
					index);
		}
		base.init();
//		base.advanceOneTurn();
		return base;
	}
	private AbstractObjectAssert<?,?> isMoveTo(Pac my_p0, Map<Pac, PacAction> actions) {
		return assertThat(actions.get(my_p0)).isInstanceOf(MoveTo.class)
			.extracting(action -> ((MoveTo) action).destination);
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_action_at_turn_106_1589394391220() {
		Playfield tested = read(Arrays.asList(
			"#################################",
			"#?#?????#?###???#   ### #     #?#",
			"#?#?###?#?###?##### ### # ### #?#",
			"#   #???#               #   #   #",
			"### #?### ##### # ##### ### # ###",
			"#             # # #             #",
			"# #?#?#?##### # # # ##### # # #?#",
			"#?#?#???????#       #       # #?#",
			"###?#####?#?#?# # # # # ##### ###",
			"#?#???????#???#   #   #       #?#",
			"#?#?#?###?###?##### ### ### # #?#",
			"#???#? ???#???????? ??#     # ??#",
			"###?#####?#?###?#?###?# ##### ###",
			"????????????###?#?###?? ????? ???",
			"#####?#####?###?#?###?#####?#####",
			"#################################"
			));
		Pac
			my_p2 = new Pac(29, 4, 2, true, Type.SCISSORS, 0, 0),
			my_p1 = new Pac(21, 7, 1, true, Type.PAPER, 1, 5),
			my_p0 = new Pac(29, 3, 0, true, Type.ROCK, 0, 0);
		tested.readGameEntities(my_p2, my_p1, my_p0);
		Map<Pac, PacAction> actions = tested.computeActions();
		isMoveTo(my_p2, actions)
			.isNotEqualTo(at(29,3))
			.isNotEqualTo(at(29,4));
		isMoveTo(my_p0, actions)
			.isNotEqualTo(at(29,4))
			.isNotEqualTo(at(29,3));
		assertThat(actions).isNotEmpty();
	}
}
