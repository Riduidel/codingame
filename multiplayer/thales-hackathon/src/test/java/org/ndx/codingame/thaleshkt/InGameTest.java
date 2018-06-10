package org.ndx.codingame.thaleshkt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.thaleshkt.entities.Flag;
import org.ndx.codingame.thaleshkt.entities.UFO;
import org.ndx.codingame.thaleshkt.playground.Playfield;
import org.ndx.codingame.thaleshkt.playground.Side;
import org.ndx.codingame.thaleshkt.status.MySide;
import org.ndx.codingame.thaleshkt.status.ThalesStatus;

public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_at__1528635669925() {
		ThalesStatus status = new ThalesStatus();
		status.set(new MySide(Side.LEFT));
		Playfield p = new Playfield(status);
		p.my.first = new UFO(1622, 2508, -160, 464, 0);
		p.my.second = new UFO(8458, 2904, 59, -733, 0);
		p.my.flag = new Flag(-1, -1);
		p.adversary.first = new UFO(530, 3154, 575, 60, 1);
		p.adversary.second = new UFO(9152, 3435, -23, -368, 0);
		p.adversary.flag = new Flag(9000, 1932);				assertThat(p.compute()).isNotEqualTo("530 3154 BOOST\n"+
		"9000 1932 BOOST");
	}
}
