package org.ndx.codingame.spring.challenge.playground;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.lib2d.Geometry.at;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.PlayerTest;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.playground.Cache;
import org.ndx.codingame.spring.challenge.playground.Playfield;

public class CacheTest {

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_fill_cache_correctly() {
		Playfield tested = PlayerTest.read(Arrays.asList(
			"#######",
			"#     #",
			"#######"
			));
		AbstractPac
			my_p1 = new Pac(3, 1, 1, false, Type.PAPER, 0, 0);
		tested.readGameEntities(my_p1);
		/* Let's make sure our cache is correctly filled */
		/* navigable points should be ok */
		assertThat(tested.cache.getLocations())
			.contains(at(1, 1), at(2, 1), at(3,1), at(4, 1), at(5, 1));
		/* Directions should be left and right */
		assertThat(tested.cache.directions.get(my_p1))
			.contains(Direction.LEFT, Direction.RIGHT);
		/* normal points cache should contains elements of length 2 */
		List<List<DiscretePoint>> navigablePointsOnP1 = 
				tested.cache.getNextPointsCache(my_p1);
		assertThat(navigablePointsOnP1).hasSize(2);
		assertThat(navigablePointsOnP1.get(1))
			.containsExactly(at(3, 1), at(2, 1));
		assertThat(navigablePointsOnP1.get(0))
			.containsExactly(at(3, 1), at(4, 1));
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Disabled @Test public void can_find_action_at_turn_121_1589440712158() {
		Playfield tested = PlayerTest.read(Arrays.asList(
			"###################################",
			"#?#???#?????#?#?###?#.#?????#??.#?#",
			"#?#?#?###?#?#?#?###?#.#?#?###?#.#?#",
			"#?? #?????#???#?????# ??#?????# ..#",
			"### #####?###?###?### ###?##### ###",
			"     ????                          ",
			"### #####?###?####### ###?##### ###",
			"#..   ..#???# ??????? #???#???? ??#",
			"#?# #?#?#?#?#?####### #?#?#?#?# #?#",
			"#?# ??#???#?#  .....  #?#???#?? #?#",
			"###?#####?#?#?#?###?#.#?#?##### ###",
			"??? ? ????????#.    #.??? ????? ???",
			"#####.#?#?#####?### ##### #?#?#####",
			"#?#??   #?????? ??? ????? #?????#?#",
			"#?#?# ###?#####?#?#?#####?###?#?#?#",
			"#???#O??#???#???#?#???#???#??O#???#",
			"###################################"
			));
		AbstractPac
			my_p1 = new Pac(4, 7, 1, true, Type.PAPER, 0, 0),
			my_p0 = new Pac(31, 3, 0, true, Type.ROCK, 0, 0),
			my_p3 = new Pac(16, 11, 3, true, Type.ROCK, 0, 0),
			my_p4 = new Pac(5, 14, 4, true, Type.PAPER, 0, 0),
			my_p2 = new Pac(21, 9, 2, true, Type.SCISSORS, 0, 0);
		tested.readGameEntities(my_p1, my_p0, my_p3, my_p4, my_p2);
		/* Let's make sure our cache is correctly filled */
		List<List<DiscretePoint>> normalPointsCache = tested.cache
				.getNextPointsCache(my_p4);
		assertThat(normalPointsCache).hasSize(2)
			.extracting(list -> list.get(0)).hasSize(2)
			.containsExactly(at(5, 14), at(4, 14));
	}

}
