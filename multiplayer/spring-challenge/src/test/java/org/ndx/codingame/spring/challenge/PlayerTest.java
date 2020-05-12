package org.ndx.codingame.spring.challenge;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.actions.MoveTo;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.playground.Playfield;
import org.ndx.codingame.spring.challenge.playground.Turn;


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
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Disabled @Test public void can_find_action_at_turn_94_1589298843610() {
		Playfield tested = read(Arrays.asList(
			"#################################",
			"### # #     # ##### #     # # ###",
			"### # # ##### ##### ##### # # ###",
			"#?#   #       # ? #       #   # #",
			"#?# ### ##### # # # ##### ### # #",
			"#         #           #         #",
			"### # # # #?### # ### # # # # ###",
			"      # #     # # #     # #      ",
			"##### # # ### # # # ### # # #####",
			"#???? # #   # #   # #   # #     #",
			"#?#?### ### # # # # # ### ### # #",
			"#?#             #             # #",
			"#?##### ### ### # ### ### ##### #",
			"#?????? #   #   # O?#   #       #",
			"#####?# #?# # # # #?# #?# # #####",
			"#################################"
			));
		Pac
			my_p2 = new Pac(10, 9, 2, true, Type.SCISSORS, 0, 0),
			my_p1 = new Pac(23, 8, 1, true, Type.PAPER, 0, 0),
			my_p0 = new Pac(25, 6, 0, true, Type.ROCK, 0, 0);
		tested.readGameEntities(my_p2, my_p1, my_p0);
		Map<Pac, PacAction> actions = tested.computeActions(223259);
		PacAction for_p1 = actions.get(my_p1);
		assertThat(for_p1).isInstanceOf(MoveTo.class)
			.extracting(m -> ((MoveTo) m).destination)
			.isEqualTo(new DiscretePoint(23, 9));
		assertThat(actions).isNotEmpty();
	}
	
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_action_at_turn_4_1589301719691() {
		Playfield tested = read(Arrays.asList(
			"#################################",
			"###.# #?????#?#####?#????.#?#?###",
			"###.# #?#####?#####?#####.#?#?###",
			"#?#   #???????#???#.   ...#???#?#",
			"#?# ###?#####?#?#?#?#####.###?#?#",
			"#.. ......#???????????#??.??????#",
			"###.#?#?#?#?###?#?###?#?# #?#?###",
			"???.??#?#?????#?#?#?????# #??????",
			"#####?#?#?###?#?#?#?###?# #?#####",
			"#?????#?#???#?#???#?#???# #?????#",
			"#?#?###?###?#?#O#O#?#?### ###?#?#",
			"#?#?????????????#????????.????#?#",
			"#?#####?###?###?#?###?###.#####?#",
			"#???????#???#?O?#?O?#???#.??????#",
			"#####?#?#?#?#?#?#?#?#?#?#.#?#####",
			"#################################"
			));
		Pac
			my_p1 = new Pac(3, 5, 1, true, Type.PAPER, 0, 0),
			my_p2 = new Pac(25, 6, 2, true, Type.SCISSORS, 0, 0),
			my_p0 = new Pac(20, 3, 0, true, Type.ROCK, 0, 0);
		tested.readGameEntities(my_p1, my_p2, my_p0);
		Map<Pac, PacAction> actions = tested.computeActions(204717);
		PacAction for_my_p0 = actions.get(my_p0);
		assertThat(for_my_p0).isInstanceOf(MoveTo.class)
			.extracting(action -> ((MoveTo) action).destination)
			.isEqualTo(new DiscretePoint(19, 3));
		assertThat(actions).isNotEmpty();
	}
}
