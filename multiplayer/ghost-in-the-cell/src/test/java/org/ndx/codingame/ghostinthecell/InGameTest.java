package org.ndx.codingame.ghostinthecell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.ghostinthecell.playground.Playfield;


public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1488655452480() {
		final Playfield p = new PlayfieldBuilder().at(37)
		.i(0, 0, 0, 0)
		.f(0).t(1).d(7).t(2).d(7).t(3).d(3).t(4).d(3).t(5).d(8).t(6).d(8).t(7).d(3).t(8).d(3).t(9).d(5).t(10).d(5)
		.i(1, -1, 0, 0)
		.f(1).t(0).d(7).t(2).d(15).t(3).d(4).t(4).d(11).t(5).d(5).t(6).d(15).t(7).d(2).t(8).d(11).t(9).d(2).t(10).d(13)
		.i(2, 1, 7, 1)
		.f(2).t(0).d(7).t(1).d(15).t(3).d(11).t(4).d(4).t(5).d(15).t(6).d(5).t(7).d(11).t(8).d(2).t(9).d(13).t(10).d(2)
		.i(3, 1, 9, 2)
		.f(3).t(0).d(3).t(1).d(4).t(2).d(11).t(4).d(8).t(5).d(3).t(6).d(12).t(7).d(1).t(8).d(7).t(9).d(1).t(10).d(10)
		.i(4, -1, 3, 2)
		.f(4).t(0).d(3).t(1).d(11).t(2).d(4).t(3).d(8).t(5).d(12).t(6).d(3).t(7).d(7).t(8).d(1).t(9).d(10).t(10).d(1)
		.i(5, 1, 10, 3)
		.f(5).t(0).d(8).t(1).d(5).t(2).d(15).t(3).d(3).t(4).d(12).t(6).d(17).t(7).d(5).t(8).d(12).t(9).d(2).t(10).d(14)
		.i(6, 1, 3, 3)
		.f(6).t(0).d(8).t(1).d(15).t(2).d(5).t(3).d(12).t(4).d(3).t(5).d(17).t(7).d(12).t(8).d(5).t(9).d(14).t(10).d(2)
		.i(7, 0, 0, 0)
		.f(7).t(0).d(3).t(1).d(2).t(2).d(11).t(3).d(1).t(4).d(7).t(5).d(5).t(6).d(12).t(8).d(8).t(9).d(1).t(10).d(10)
		.i(8, 0, 0, 0)
		.f(8).t(0).d(3).t(1).d(11).t(2).d(2).t(3).d(7).t(4).d(1).t(5).d(12).t(6).d(5).t(7).d(8).t(9).d(10).t(10).d(1)
		.i(9, 1, 7, 2)
		.f(9).t(0).d(5).t(1).d(2).t(2).d(13).t(3).d(1).t(4).d(10).t(5).d(2).t(6).d(14).t(7).d(1).t(8).d(10).t(10).d(12)
		.i(10, 1, 1, 2)
		.f(10).t(0).d(5).t(1).d(13).t(2).d(2).t(3).d(10).t(4).d(1).t(5).d(14).t(6).d(2).t(7).d(10).t(8).d(1).t(9).d(12)
		.b(1, 2, 1)
		.t(1,4,-1,4,1)
		.t(1,4,-1,2,3)
		.b(1, 4, 1)
		.t(5,2,1,2,1)
		.t(5,2,1,2,2)
		.t(6,2,1,1,4)
		.t(3,4,1,7,1)
		.t(3,10,1,6,6)
		.t(5,4,1,11,2)
		.t(9,4,1,8,2)
		.t(5,10,1,3,12)
		.t(6,10,1,1,1)
		.t(6,10,1,4,2)
		.t(9,10,1,8,8)
		.t(9,10,1,2,9)
		.t(9,10,1,3,10)
;
		assertThat(p.compute()).doesNotContain("MOVE 10 4 -3");
	}
}
