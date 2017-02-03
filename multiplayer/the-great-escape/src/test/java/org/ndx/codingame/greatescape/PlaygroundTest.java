package org.ndx.codingame.greatescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.greatescape.entities.Gamer;
import org.ndx.codingame.greatescape.entities.Orientation;
import org.ndx.codingame.greatescape.entities.Wall;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;

/**
 * Some basic conceptual tests
 * @author ndelsaux
 *
 */
public class PlaygroundTest {
	public static class PlaygroundWritingTest {
		@Test public void simple_squared_playfield_from_debug_with_no_walls() {
			Gamer me = null;
			final Playfield tested = new Playfield(2, 2);
			tested.set(0, 0, me = new Gamer(0, 0, 10, Direction.LEFT));
			tested.set(4, 4, new Gamer(2, 2, 10, Direction.RIGHT));
			assertThat(tested.toUnitTestString(me))
				.contains("tested.set(0, 0, me = new Gamer(0, 0, 10, Direction.LEFT))")
				.contains("tested.set(4, 4, new Gamer(2, 2, 10, Direction.RIGHT)");
		}
		@Test public void simple_squared_playfield_from_debug_with_center_wall() {
			Gamer me = null;
			final Playfield tested = new Playfield(2, 2);
			tested.set(0, 0, me = new Gamer(0, 0, 10, Direction.LEFT));
			tested.set(4, 4, new Gamer(2, 2, 10, Direction.RIGHT));
			tested.set(1, 0, new Wall(Orientation.V));
			assertThat(tested.toUnitTestString(me))
				.contains("tested.set(0, 0, me = new Gamer(0, 0, 10, Direction.LEFT))")
				.contains("tested.set(4, 4, new Gamer(2, 2, 10, Direction.RIGHT)")
				.contains("tested.set(1, 0, new Wall(Orientation.V));");
		}
		// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
		@Test public void simple_squared_playfield_from_main_with_no_walls() {
			Gamer me = null;
			final Playfield tested = new Playfield(2, 2);
			tested.setAt(0, 0, me = new Gamer(0, 0, 10, Direction.LEFT));
			tested.setAt(2, 2, new Gamer(2, 2, 10, Direction.RIGHT));
			assertThat(tested.toUnitTestString(me))
				.contains("tested.set(0, 0, me = new Gamer(0, 0, 10, Direction.LEFT))")
				.contains("tested.set(4, 4, new Gamer(2, 2, 10, Direction.RIGHT)");
		}
		@Test public void simple_squared_playfield_from_main_with_center_wall() {
			Gamer me = null;
			final Playfield tested = new Playfield(2, 2);
			tested.setAt(0, 0, me = new Gamer(0, 0, 10, Direction.LEFT));
			tested.setAt(2, 2, new Gamer(2, 2, 10, Direction.RIGHT));
			tested.set(1, 0, new Wall(Orientation.V));
			assertThat(tested.toUnitTestString(me))
				.contains("tested.set(0, 0, me = new Gamer(0, 0, 10, Direction.LEFT))")
				.contains("tested.set(4, 4, new Gamer(2, 2, 10, Direction.RIGHT)")
				.contains("tested.set(1, 0, new Wall(Orientation.V));");
		}
	}
}
