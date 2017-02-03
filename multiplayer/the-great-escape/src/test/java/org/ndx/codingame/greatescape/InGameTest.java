package org.ndx.codingame.greatescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.ndx.codingame.greatescape.entities.Gamer;
import org.ndx.codingame.greatescape.entities.Orientation;
import org.ndx.codingame.greatescape.entities.Wall;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;

public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1485867579448() {
		Gamer me = null;
		final Playfield tested = new Playfield(9, 9);
		tested.set(13, 0, new Wall(Orientation.V));
		tested.set(13, 1, new Wall(Orientation.V));
		tested.set(14, 1, new Wall(Orientation.H));
		tested.set(15, 1, new Wall(Orientation.H));
		tested.set(16, 1, new Wall(Orientation.H));
		tested.set(13, 2, new Wall(Orientation.V));
		tested.set(13, 4, new Wall(Orientation.V));
		tested.set(13, 5, new Wall(Orientation.V));
		tested.set(2, 6, new Gamer(1, 3, 6, Direction.LEFT));
		tested.set(13, 6, new Wall(Orientation.V));
		tested.set(0, 16, me = new Gamer(0, 8, 3, Direction.RIGHT));
		assertThat(me).isNotNull();
		assertThat(me.compute(tested).toCodingame()).isNotNull();
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1485870470184() {
		Gamer me = null;
		final Playfield tested = new Playfield(9, 9);
		tested.set(0, 0, new Gamer(0, 0, 6, Direction.DOWN));
		tested.set(2, 14, new Gamer(1, 7, 6, Direction.RIGHT));
		tested.set(16, 16, me = new Gamer(8, 8, 6, Direction.LEFT));
		assertThat(me).isNotNull();
		assertThat(me.compute(tested).toCodingame()).doesNotStartWith("1 1 V");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1485886815961() {
		Gamer me = null;
		final Playfield tested = new Playfield(9, 9);
		tested.set(0, 1, new Wall(Orientation.H));
		tested.set(1, 1, new Wall(Orientation.H));
		tested.set(2, 1, new Wall(Orientation.H));
		tested.set(4, 14, new Gamer(2, 7, 6, Direction.RIGHT));
		tested.set(16, 16, me = new Gamer(8, 8, 5, Direction.LEFT));
		assertThat(me).isNotNull();
		assertThat(me.compute(tested).toCodingame()).startsWith("2 7 V");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test @Ignore public void can_find_move_1485970204145() {
		Gamer me = null;
		final Playfield tested = new Playfield(9, 9);
		tested.set(0, 1, new Wall(Orientation.H));
		tested.set(1, 1, new Wall(Orientation.H));
		tested.set(2, 1, new Wall(Orientation.H));
		tested.set(16, 2, me = new Gamer(8, 1, 5, Direction.LEFT));
		tested.set(4, 12, new Gamer(2, 6, 6, Direction.RIGHT));
		assertThat(me).isNotNull();
		assertThat(me.compute(tested).toCodingame()).startsWith("3 6 V");
	}
}
