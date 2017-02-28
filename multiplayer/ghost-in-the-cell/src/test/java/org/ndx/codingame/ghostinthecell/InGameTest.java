package org.ndx.codingame.ghostinthecell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.ghostinthecell.playground.Playfield;


public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_move_1488305182226() {
		final Playfield p = new Playfield(0);
		p.setFactoryInfos(0, 0, 0, 0);
		p.f(0).t(1).d(3).t(2).d(3).t(3).d(6).t(4).d(6).t(5).d(2).t(6).d(2).t(7).d(6).t(8).d(6).t(9).d(5).t(10).d(5);
		p.setFactoryInfos(1, -1, 24, 3);
		p.f(1).t(0).d(3).t(2).d(8).t(3).d(2).t(4).d(11).t(5).d(3).t(6).d(6).t(7).d(2).t(8).d(11).t(9).d(1).t(10).d(9);
		p.t(1,2,1,18,2);
		p.t(1,2,1,3,3);
		p.setFactoryInfos(2, -1, 45, 3);
		p.f(2).t(0).d(3).t(1).d(8).t(3).d(11).t(4).d(2).t(5).d(6).t(6).d(3).t(7).d(11).t(8).d(2).t(9).d(9).t(10).d(1);
		p.t(2,9,-1,8,9);
		p.setFactoryInfos(3, 0, 0, 0);
		p.f(3).t(0).d(6).t(1).d(2).t(2).d(11).t(4).d(14).t(5).d(6).t(6).d(8).t(7).d(1).t(8).d(14).t(9).d(3).t(10).d(12);
		p.setFactoryInfos(4, 0, 0, 0);
		p.f(4).t(0).d(6).t(1).d(11).t(2).d(2).t(3).d(14).t(5).d(8).t(6).d(6).t(7).d(14).t(8).d(1).t(9).d(12).t(10).d(3);
		p.setFactoryInfos(5, 0, 0, 0);
		p.f(5).t(0).d(2).t(1).d(3).t(2).d(6).t(3).d(6).t(4).d(8).t(6).d(6).t(7).d(5).t(8).d(9).t(9).d(2).t(10).d(8);
		p.setFactoryInfos(6, 0, 0, 0);
		p.f(6).t(0).d(2).t(1).d(6).t(2).d(3).t(3).d(8).t(4).d(6).t(5).d(6).t(7).d(9).t(8).d(5).t(9).d(8).t(10).d(2);
		p.setFactoryInfos(7, 1, 33, 3);
		p.f(7).t(0).d(6).t(1).d(2).t(2).d(11).t(3).d(1).t(4).d(14).t(5).d(5).t(6).d(9).t(8).d(14).t(9).d(1).t(10).d(12);
		p.t(7,2,1,6,2);
		p.t(7,2,1,6,3);
		p.setFactoryInfos(8, -1, 9, 3);
		p.f(8).t(0).d(6).t(1).d(11).t(2).d(2).t(3).d(14).t(4).d(1).t(5).d(9).t(6).d(5).t(7).d(14).t(9).d(12).t(10).d(1);
		p.t(8,2,-1,3,1);
		p.t(8,2,-1,3,2);
		p.setFactoryInfos(9, 1, 29, 3);
		p.f(9).t(0).d(5).t(1).d(1).t(2).d(9).t(3).d(3).t(4).d(12).t(5).d(2).t(6).d(8).t(7).d(1).t(8).d(12).t(10).d(11);
		p.t(9,2,1,3,1);
		p.setFactoryInfos(10, -1, 6, 3);
		p.f(10).t(0).d(5).t(1).d(9).t(2).d(1).t(3).d(12).t(4).d(3).t(5).d(8).t(6).d(2).t(7).d(12).t(8).d(1).t(9).d(11);
		p.t(10,2,-1,3,1);
		final String computed = p.compute();
		assertThat(computed).isNotNull();
	}
}
