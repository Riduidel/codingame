package org.ndx.codingame.ghostinthecell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.ghostinthecell.playground.Playfield;


public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1488305182226() {
		final Playfield p = new PlayfieldBuilder()
		.i(0, 0, 0, 0)
		.f(0).t(1).d(3).t(2).d(3).t(3).d(6).t(4).d(6).t(5).d(2).t(6).d(2).t(7).d(6).t(8).d(6).t(9).d(5).t(10).d(5)
		.i(1, -1, 24, 3)
		.f(1).t(0).d(3).t(2).d(8).t(3).d(2).t(4).d(11).t(5).d(3).t(6).d(6).t(7).d(2).t(8).d(11).t(9).d(1).t(10).d(9)
		.t(1,2,1,18,2)
		.t(1,2,1,3,3)
		.i(2, -1, 45, 3)
		.f(2).t(0).d(3).t(1).d(8).t(3).d(11).t(4).d(2).t(5).d(6).t(6).d(3).t(7).d(11).t(8).d(2).t(9).d(9).t(10).d(1)
		.t(2,9,-1,8,9)
		.i(3, 0, 0, 0)
		.f(3).t(0).d(6).t(1).d(2).t(2).d(11).t(4).d(14).t(5).d(6).t(6).d(8).t(7).d(1).t(8).d(14).t(9).d(3).t(10).d(12)
		.i(4, 0, 0, 0)
		.f(4).t(0).d(6).t(1).d(11).t(2).d(2).t(3).d(14).t(5).d(8).t(6).d(6).t(7).d(14).t(8).d(1).t(9).d(12).t(10).d(3)
		.i(5, 0, 0, 0)
		.f(5).t(0).d(2).t(1).d(3).t(2).d(6).t(3).d(6).t(4).d(8).t(6).d(6).t(7).d(5).t(8).d(9).t(9).d(2).t(10).d(8)
		.i(6, 0, 0, 0)
		.f(6).t(0).d(2).t(1).d(6).t(2).d(3).t(3).d(8).t(4).d(6).t(5).d(6).t(7).d(9).t(8).d(5).t(9).d(8).t(10).d(2)
		.i(7, 1, 33, 3)
		.f(7).t(0).d(6).t(1).d(2).t(2).d(11).t(3).d(1).t(4).d(14).t(5).d(5).t(6).d(9).t(8).d(14).t(9).d(1).t(10).d(12)
		.t(7,2,1,6,2)
		.t(7,2,1,6,3)
		.i(8, -1, 9, 3)
		.f(8).t(0).d(6).t(1).d(11).t(2).d(2).t(3).d(14).t(4).d(1).t(5).d(9).t(6).d(5).t(7).d(14).t(9).d(12).t(10).d(1)
		.t(8,2,-1,3,1)
		.t(8,2,-1,3,2)
		.i(9, 1, 29, 3)
		.f(9).t(0).d(5).t(1).d(1).t(2).d(9).t(3).d(3).t(4).d(12).t(5).d(2).t(6).d(8).t(7).d(1).t(8).d(12).t(10).d(11)
		.t(9,2,1,3,1)
		.i(10, -1, 6, 3)
		.f(10).t(0).d(5).t(1).d(9).t(2).d(1).t(3).d(12).t(4).d(3).t(5).d(8).t(6).d(2).t(7).d(12).t(8).d(1).t(9).d(11)
		.t(10,2,-1,3,1);
		final String computed = p.compute();
		assertThat(computed).isNotNull();
	}
}
