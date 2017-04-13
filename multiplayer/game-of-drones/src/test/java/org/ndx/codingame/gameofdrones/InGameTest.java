package org.ndx.codingame.gameofdrones;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.ndx.codingame.gameofdrones.entities.Drone;
import org.ndx.codingame.gameofdrones.entities.Zone;
import org.ndx.codingame.gameofdrones.playground.Playfield;

public class InGameTest {

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_moves_1491420023187() {
		final List<Zone> z=new ArrayList<>();
			z.add(new Zone(544.0, 1296.0, 0));
			z.add(new Zone(457.0, 510.0, 1));
			z.add(new Zone(2064.0, 1077.0, 0));
			z.add(new Zone(3423.0, 629.0, 0));
		final List<Drone> d=new ArrayList<>();
			d.add(new Drone(2064.0, 1077.0, 2064.0, 1077.0, 0, 0));
			d.add(new Drone(2064.0, 1077.0, 2064.0, 1077.0, 0, 1));
			d.add(new Drone(544.0, 1296.0, 544.0, 1296.0, 0, 2));
			d.add(new Drone(3423.0, 629.0, 3423.0, 629.0, 0, 3));
			d.add(new Drone(2064.0, 1077.0, 2064.0, 1077.0, 0, 4));
			d.add(new Drone(3423.0, 629.0, 3423.0, 629.0, 0, 5));
			d.add(new Drone(2064.0, 1077.0, 2064.0, 1077.0, 0, 6));
			d.add(new Drone(2064.0, 1077.0, 2064.0, 1077.0, 0, 7));
			d.add(new Drone(2064.0, 1077.0, 2064.0, 1077.0, 0, 8));
			d.add(new Drone(2824.0, 827.0, 2729.0, 858.0, 1, 0));
			d.add(new Drone(2644.0, 810.0, 2741.0, 787.0, 1, 1));
			d.add(new Drone(457.0, 510.0, 467.0, 600.0, 1, 2));
			d.add(new Drone(1211.0, 776.0, 1117.0, 743.0, 1, 3));
			d.add(new Drone(2824.0, 827.0, 2729.0, 858.0, 1, 4));
			d.add(new Drone(2644.0, 810.0, 2741.0, 787.0, 1, 5));
			d.add(new Drone(457.0, 510.0, 467.0, 600.0, 1, 6));
			d.add(new Drone(1211.0, 776.0, 1117.0, 743.0, 1, 7));
			d.add(new Drone(2824.0, 827.0, 2729.0, 858.0, 1, 8));
		final Playfield playfield = new Playfield();
		playfield.addAllZones(z);
		playfield.addAllDrones(d);
		playfield.setOwner(0);
		assertThat(playfield.computeMoves()).isNotEmpty();
	}
}
