package org.ndx.codingame.ghostinthecell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.ghostinthecell.playground.Playfield;


public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1488276373601() {
		final Playfield tested = new Playfield();
		tested.setFactoryInfos(0, 0, 0, 0);
		tested.connect(0, 1, 6);
		tested.connect(0, 2, 6);
		tested.connect(0, 3, 1);
		tested.connect(0, 4, 1);
		tested.connect(0, 5, 2);
		tested.connect(0, 6, 2);
		tested.setFactoryInfos(1, 1, 6, 3);
		tested.connect(1, 0, 6);
		tested.connect(1, 2, 13);
		tested.connect(1, 3, 3);
		tested.connect(1, 4, 9);
		tested.connect(1, 5, 3);
		tested.connect(1, 6, 9);
		tested.setTroop(1, 6, 1, 1, 9);
		tested.setTroop(1, 6, 1, 10, 8);
		tested.setFactoryInfos(2, -1, 12, 3);
		tested.connect(2, 0, 6);
		tested.connect(2, 1, 13);
		tested.connect(2, 3, 9);
		tested.connect(2, 4, 3);
		tested.connect(2, 5, 9);
		tested.connect(2, 6, 3);
		tested.setFactoryInfos(3, 1, 4, 3);
		tested.connect(3, 0, 1);
		tested.connect(3, 1, 3);
		tested.connect(3, 2, 9);
		tested.connect(3, 4, 4);
		tested.connect(3, 5, 1);
		tested.connect(3, 6, 5);
		tested.setFactoryInfos(4, -1, 26, 3);
		tested.connect(4, 0, 1);
		tested.connect(4, 1, 9);
		tested.connect(4, 2, 3);
		tested.connect(4, 3, 4);
		tested.connect(4, 5, 5);
		tested.connect(4, 6, 1);
		tested.setFactoryInfos(5, 1, 10, 3);
		tested.connect(5, 0, 2);
		tested.connect(5, 1, 3);
		tested.connect(5, 2, 9);
		tested.connect(5, 3, 1);
		tested.connect(5, 4, 5);
		tested.connect(5, 6, 6);
		tested.setFactoryInfos(6, 0, 10, 3);
		tested.connect(6, 0, 2);
		tested.connect(6, 1, 9);
		tested.connect(6, 2, 3);
		tested.connect(6, 3, 5);
		tested.connect(6, 4, 1);
		tested.connect(6, 5, 6);
		final String computed = tested.compute();
		assertThat(computed)
			.isNotNull()
			.doesNotContain("MOVE 5 0 11");
	}
}
