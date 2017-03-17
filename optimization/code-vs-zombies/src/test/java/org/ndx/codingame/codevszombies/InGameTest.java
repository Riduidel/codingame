package org.ndx.codingame.codevszombies;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.codevszombies.entities.Ash;
import org.ndx.codingame.codevszombies.entities.Human;
import org.ndx.codingame.codevszombies.entities.Zombie;
import org.ndx.codingame.codevszombies.playground.Playfield;

public class InGameTest {

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void first_test() {
		final Playfield playfield = new Playfield();
		playfield.add(new Human(0, 8250, 8250));
		playfield.add(new Zombie(0, 8250, 8599, 8250, 8599));
		playfield.add(new Ash(0, 0));
		assertThat(playfield.compute()).doesNotStartWith("0 0");
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void test_of_split_second_reflex() {
		final Playfield playfield=new Playfield();
			playfield.add(new Human(0, 3000, 4500));
			playfield.add(new Human(1, 14000, 4500));
			playfield.add(new Zombie(0, 2500, 4500, 2900, 4500));
			playfield.add(new Zombie(1, 15500, 6180, 15260, 6180));
		playfield.add(new Ash(8000, 8000));
		assertThat(playfield.compute()).isEqualTo("14000 4500");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_with_no_human() {
		final Playfield playfield=new Playfield();
			playfield.add(new Zombie(0, 950, 5963, 1348, 5963));
		playfield.add(new Ash(7646, 7646));
		assertThat(playfield.compute()).isEqualTo("8000 4500");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1489746853228() {
		final Playfield playfield=new Playfield();
			playfield.add(new Human(0, 4000, 2250));
			playfield.add(new Human(1, 4000, 6750));
			playfield.add(new Human(2, 12000, 2250));
			playfield.add(new Human(3, 12000, 6750));
			playfield.add(new Zombie(0, 4000, 2975, 4000, 2975));
			playfield.add(new Zombie(1, 12000, 2975, 12000, 2975));
			playfield.add(new Zombie(2, 4000, 4100, 4000, 4100));
			playfield.add(new Zombie(3, 12000, 4100, 12000, 4100));
			playfield.add(new Zombie(4, 4000, 6025, 4000, 6025));
			playfield.add(new Zombie(5, 12000, 6025, 12000, 6025));
			playfield.add(new Zombie(6, 6000, 2250, 5600, 2250));
			playfield.add(new Zombie(7, 8000, 2650, 8000, 2650));
			playfield.add(new Zombie(8, 10000, 2250, 10400, 2250));
			playfield.add(new Zombie(9, 6000, 6750, 5600, 6750));
			playfield.add(new Zombie(10, 8000, 6350, 8000, 6350));
			playfield.add(new Zombie(11, 10000, 6750, 10400, 6750));
		playfield.add(new Ash(8000, 8000));
		assertThat(playfield.compute()).isNotEqualTo("8000 4500");
	}
}
