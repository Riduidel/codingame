package org.ndx.codingame.thaleshkt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.ndx.codingame.thaleshkt.entities.Flag;
import org.ndx.codingame.thaleshkt.entities.UFO;
import org.ndx.codingame.thaleshkt.playground.Playfield;
import org.ndx.codingame.thaleshkt.playground.Side;

public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void second_player_should_direct_to_second_nearest_flag() {
		Playfield p = new Playfield();
		p.my.first = new UFO(855, 2067, 154, -187, 0);
		p.my.second = new UFO(575, 4945, 32, -241, 0);
		p.my.flag = new Flag(1000, 1891);				p.adversary.first = new UFO(8942, 2461, -241, -16, 0);
		p.adversary.second = new UFO(8984, 5281, -223, -94, 0);
		p.adversary.flag = new Flag(9000, 6109);				p.side = Side.LEFT;
		assertThat(p.compute()).isNotEqualTo("1000 1891 100\n"+
		"1000 1891 100");
	}
}
