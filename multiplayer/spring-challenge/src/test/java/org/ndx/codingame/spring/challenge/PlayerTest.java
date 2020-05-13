package org.ndx.codingame.spring.challenge;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
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
	private void isMoveTo(Pac my_p0, Map<Pac, PacAction> actions, int x, int y) {
		assertThat(actions.get(my_p0)).isInstanceOf(MoveTo.class)
			.extracting(action -> ((MoveTo) action).destination)
			.isEqualTo(new DiscretePoint(x, y));
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_action_at_turn_81_1589362518196() {
		Playfield tested = read(Arrays.asList(
			"#################################",
			"###ddd# ??# #?? #???#d#?? #ddd###",
			"#####d# ### ### #?###d### #d#####",
			"#ddddd#   # #   #???#d#   #ddd d#",
			"#d#d### # # # #####?#d# # ###d#d#",
			"#d#     #               #     #d#",
			"### # # ##### #####?##### # # ###",
			"#   #         #####???? ? ? # dd#",
			"#?# ### # # # #####?#?# # ### #d#",
			"#?#       # ? ????????#       #d#",
			"##### # ##### #?#?#?##### # #####",
			"#O#   # ????? ??#???????? #   #d#",
			"#?# # # #?#?###?#?###?#?# # # # #",
			"#??.# # #?#?#???#???#?#?# # #   #",
			"#################################"
			));
		Pac
			my_p3 = new Pac(3, 9, 3, true, Type.ROCK, 0, 0),
			my_p1 = new Pac(29, 9, 1, true, Type.PAPER, 0, 0),
			my_p2 = new Pac(31, 12, 2, true, Type.SCISSORS, 0, 0),
			his_p1 = new Pac(7, 9, 1, false, Type.PAPER, 3, 7),
			my_p0 = new Pac(3, 12, 0, true, Type.ROCK, 0, 0);
		tested.readGameEntities(my_p3, my_p1, my_p2, his_p1, my_p0);
		Map<Pac, PacAction> actions = tested.computeActions(-1);
		assertThat(actions).isNotEmpty();
	}
}
