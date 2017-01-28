package org.ndx.codingame.greatescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.greatescape.actions.Action;
import org.ndx.codingame.greatescape.entities.Gamer;
import org.ndx.codingame.greatescape.entities.Orientation;
import org.ndx.codingame.greatescape.entities.Wall;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;

public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1485589307253() {
		Gamer me = null;
		final Playfield tested = new Playfield(9, 9);
		tested.set(4, 0, me = new Gamer(2, 0, 10, Direction.LEFT));
		tested.set(14, 16, new Gamer(7, 8, 10, Direction.RIGHT));
		final Action action = me.compute(tested);
		assertThat(action.toString()).isEqualTo("7 7 V");
	}
	@Test public void can_find_move_1485589307253_modified() {
		Gamer me = null;
		final Playfield tested = new Playfield(9, 9);
		tested.set(4, 0, me = new Gamer(2, 0, 10, Direction.LEFT));
		tested.set(16, 16, new Gamer(8, 8, 10, Direction.RIGHT));
		final Action action = me.compute(tested);
		assertThat(action.toString()).isEqualTo("LEFT");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1485632066775() {
		Gamer me = null;
		final Playfield tested = new Playfield(9, 9);
		tested.set(16, 2, me = new Gamer(8, 1, 9, Direction.LEFT));
		tested.set(3, 2, new Wall(Orientation.V));
		tested.set(2, 10, new Gamer(1, 5, 9, Direction.RIGHT));
		tested.set(5, 10, new Wall(Orientation.V));
		// Do not put a vertical wall against enemy
		assertThat(me.compute(tested).toString()).doesNotEndWith("V");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1485634320465() {
		Gamer me = null;
		final Playfield tested = new Playfield(9, 9);
		tested.set(14, 10, me = new Gamer(7, 5, 6, Direction.LEFT));
		tested.set(4, 16, new Gamer(2, 8, 6, Direction.RIGHT));
		assertThat(me.compute(tested).toString()).isNotEqualTo("3 8 V");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1485635460408() {
		Gamer me = null;
		final Playfield tested = new Playfield(9, 9);
		tested.set(14, 0, me = new Gamer(7, 0, 5, Direction.LEFT));
		tested.set(3, 2, new Wall(Orientation.V));
		tested.set(3, 3, new Wall(Orientation.V));
		tested.set(3, 6, new Wall(Orientation.V));
		tested.set(3, 7, new Wall(Orientation.V));
		tested.set(3, 10, new Wall(Orientation.V));
		tested.set(3, 11, new Wall(Orientation.V));
		tested.set(4, 12, new Gamer(2, 6, 2, Direction.RIGHT));
		tested.set(7, 12, new Wall(Orientation.V));
		tested.set(7, 13, new Wall(Orientation.V));
		tested.set(3, 14, new Wall(Orientation.V));
		tested.set(3, 15, new Wall(Orientation.V));
		assertThat(me.compute(tested).toString()).isNotEqualTo("UP");
	}
}
