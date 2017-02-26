package org.ndx.codingame.ghostinthecell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.ghostinthecell.playground.Playfield;


public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1488120964580() {
		final Playfield tested = new Playfield();
		tested.setFactoryInfos(0, 0, 0, 0);
		tested.connect(0, 1, 2);
		tested.connect(0, 2, 2);
		tested.connect(0, 3, 3);
		tested.connect(0, 4, 3);
		tested.connect(0, 5, 4);
		tested.connect(0, 6, 4);
		tested.connect(0, 7, 6);
		tested.connect(0, 8, 6);
		tested.setFactoryInfos(1, 1, 28, 1);
		tested.connect(1, 0, 2);
		tested.connect(1, 2, 6);
		tested.connect(1, 3, 3);
		tested.connect(1, 4, 6);
		tested.connect(1, 5, 1);
		tested.connect(1, 6, 8);
		tested.connect(1, 7, 4);
		tested.connect(1, 8, 9);
		tested.setFactoryInfos(2, -1, 28, 1);
		tested.connect(2, 0, 2);
		tested.connect(2, 1, 6);
		tested.connect(2, 3, 6);
		tested.connect(2, 4, 3);
		tested.connect(2, 5, 8);
		tested.connect(2, 6, 1);
		tested.connect(2, 7, 9);
		tested.connect(2, 8, 4);
		tested.setFactoryInfos(3, 0, 6, 2);
		tested.connect(3, 0, 3);
		tested.connect(3, 1, 3);
		tested.connect(3, 2, 6);
		tested.connect(3, 4, 7);
		tested.connect(3, 5, 1);
		tested.connect(3, 6, 8);
		tested.connect(3, 7, 1);
		tested.connect(3, 8, 10);
		tested.setFactoryInfos(4, 0, 6, 2);
		tested.connect(4, 0, 3);
		tested.connect(4, 1, 6);
		tested.connect(4, 2, 3);
		tested.connect(4, 3, 7);
		tested.connect(4, 5, 8);
		tested.connect(4, 6, 1);
		tested.connect(4, 7, 10);
		tested.connect(4, 8, 1);
		tested.setFactoryInfos(5, 0, 13, 3);
		tested.connect(5, 0, 4);
		tested.connect(5, 1, 1);
		tested.connect(5, 2, 8);
		tested.connect(5, 3, 1);
		tested.connect(5, 4, 8);
		tested.connect(5, 6, 10);
		tested.connect(5, 7, 1);
		tested.connect(5, 8, 11);
		tested.setFactoryInfos(6, 0, 13, 3);
		tested.connect(6, 0, 4);
		tested.connect(6, 1, 8);
		tested.connect(6, 2, 1);
		tested.connect(6, 3, 8);
		tested.connect(6, 4, 1);
		tested.connect(6, 5, 10);
		tested.connect(6, 7, 11);
		tested.connect(6, 8, 1);
		tested.setFactoryInfos(7, 0, 3, 1);
		tested.connect(7, 0, 6);
		tested.connect(7, 1, 4);
		tested.connect(7, 2, 9);
		tested.connect(7, 3, 1);
		tested.connect(7, 4, 10);
		tested.connect(7, 5, 1);
		tested.connect(7, 6, 11);
		tested.connect(7, 8, 13);
		tested.setFactoryInfos(8, 0, 3, 1);
		tested.connect(8, 0, 6);
		tested.connect(8, 1, 9);
		tested.connect(8, 2, 4);
		tested.connect(8, 3, 10);
		tested.connect(8, 4, 1);
		tested.connect(8, 5, 11);
		tested.connect(8, 6, 1);
		tested.connect(8, 7, 13);
		assertThat(tested.compute()).isNotEqualTo("WAIT");
	}
}
