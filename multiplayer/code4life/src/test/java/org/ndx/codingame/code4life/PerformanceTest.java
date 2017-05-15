package org.ndx.codingame.code4life;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.ndx.codingame.code4life.entities.MoleculeStore;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;

public class PerformanceTest {
	private static final int PERCENTILE = 100;
	private static final int THREAD_COUNT = 1;
	private static final int INVOCATION_COUNT = 30;
	@Rule
	public ContiPerfRule i = new ContiPerfRule();

	@BeforeClass
	public static void waitForVisualVM() {
		System.err.println("Waiting for VisualVM on this breakpoint");
	}

	@PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	@Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1494772094411() {
		final List<Robot> robots = new ArrayList<>();
		robots.add(new Robot("SAMPLES", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		robots.add(new Robot("MOLECULES", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> samples = new ArrayList<>();
		final Playfield playfield = new Playfield();
		playfield.addAllRobots(robots);
		playfield.addAllSamples(samples);
		playfield.addAllAvailable(MoleculeStore.toMap(6, 6, 6, 6, 6));
		assertThat(playfield.computeMoves()).isNotEmpty();
	}
}
