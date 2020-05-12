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
		base.advanceOneTurn();
		return base;
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Disabled @Test public void can_find_move_1589274747869() {
		Playfield tested = read(Arrays.asList(
			"#################################",
			"###.# #     # ##### #     # # ###",
			"###.# # ##### ##### ##### # # ###",
			"# #   #       #   #       #   # #",
			"# # ### ##### # # # ##### ### # #",
			"#         #           #         #",
			"### # # # # ### # ### # # # # ###",
			"      # #     # # #     # #      ",
			"##### # # ### # # # ### # # #####",
			"#     # #   # #   # #   # #     #",
			"# # ### ### # # # # # ### ### # #",
			"# #             #             # #",
			"# ##### ### ### #.### ### ##### #",
			"#       #   #   #.O #   #       #",
			"##### # # # # # #.# # # # # #####",
			"#################################"
			));
		Pac
			my_p0 = new Pac(17, 4, 0, true, Type.ROCK, 0, 0),
			my_p1 = new Pac(3, 7, 1, true, Type.PAPER, 0, 0),
			his_p1 = new Pac(17, 7, 1, false, Type.PAPER, 0, 7),
			my_p2 = new Pac(17, 10, 2, true, Type.SCISSORS, 0, 0);
		tested.readGameEntities(my_p0, my_p1, his_p1, my_p2);
		Map<Pac, PacAction> actions = tested.computeActions(382);
		assertThat(actions.get(my_p2)).isInstanceOf(MoveTo.class)
			.extracting(action -> ((MoveTo) action).destination)
			.isEqualTo(new DiscretePoint(17, 11));
		assertThat(actions).isNotEmpty();
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1589276062872() {
		Playfield tested = read(Arrays.asList(
			"#################################",
			"### # #     # ##### #    .# # ###",
			"### # # ##### ##### #####.# # ###",
			"# #   #       #   #...    #   # #",
			"# # ### ##### # # # #####.### # #",
			"#         #           #  .      #",
			"### # # # # ### # ### # #.# # ###",
			"      # #     # # #     # #      ",
			"##### # # ### # # # ### # # #####",
			"#     # #   # #   # #   # #     #",
			"# # ### ### # #O#O# # ### ### # #",
			"# #             #        .    # #",
			"# ##### ### ### # ### ###.##### #",
			"#       #   # O # O #   #.      #",
			"##### # # # # # # # # # #.# #####",
			"#################################"
			));
		Pac
			my_p1 = new Pac(5, 2, 1, true, Type.PAPER, 0, 0),
			my_p0 = new Pac(25, 3, 0, true, Type.ROCK, 0, 0),
			my_p2 = new Pac(25, 7, 2, true, Type.SCISSORS, 0, 0);
		tested.readGameEntities(my_p1, my_p0, my_p2);
		Map<Pac, PacAction> actions = tested.computeActions(506);
		assertThat(actions.get(my_p1)).isInstanceOf(MoveTo.class)
			.extracting(action -> ((MoveTo) action).destination)
			.isNotEqualTo(new DiscretePoint(5, 1));
		assertThat(actions).isNotEmpty();
	}	
}
