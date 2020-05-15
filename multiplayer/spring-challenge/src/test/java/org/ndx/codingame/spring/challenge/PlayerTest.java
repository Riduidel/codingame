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
	@Test public void can_find_action_at_turn_2_1589551831923() {
		Playfield tested = read(Arrays.asList(
			"###############################",
			"#.??#?#?###???#?#???###?#.#???#",
			"#.#?#?#O###?###?###?###O#.#?#?#",
			"# #???#. .e......... m .# ..#?#",
			"# #?#?###?#?#?###?#?#.###m#?#?#",
			"#.#?# ??????#?#?#?#...... #?#?#",
			"###?#?###?###?#?#?###.###.#?###",
			"#???#???#???#?????#??.#??.#???#",
			"#?#?###?#?#?#?###?#?#.#?###?#?#",
			"#?#?????#?#?????????#.#?????#?#",
			"###?#?#?#?#?#?###?#?#.#?#?#?###",
			"  ..#?#???#?#?????#?#.??#?#. .e",
			"###############################"
			));
		Pac
			his_p0 = new Pac(8, 3, 0, false, Type.ROCK, 4, 8),
			my_p1 = new Pac(1, 11, 1, true, Type.SCISSORS, 0, 9),
			my_p3 = new Pac(25, 5, 3, true, Type.ROCK, 0, 0),
			his_p1 = new Pac(28, 11, 1, false, Type.PAPER, 4, 8),
			my_p2 = new Pac(1, 3, 2, true, Type.SCISSORS, 5, 9),
			my_p0 = new Pac(22, 3, 0, true, Type.ROCK, 0, 0);
		tested.readGameEntities(his_p0, my_p1, my_p3, his_p1, my_p2, my_p0);
		Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */90);
		isMoveTo(my_p2, actions).isEqualTo(at(1, 1));
		tested.advanceOneTurn();
		tested.advanceOneTurn();
		assertThat(tested.get(my_p2)).isEqualTo(Ground.instance);
	}
}
