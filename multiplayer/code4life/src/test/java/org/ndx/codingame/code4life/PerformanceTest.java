package org.ndx.codingame.code4life;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.ndx.codingame.code4life.entities.MoleculeStore;
import org.ndx.codingame.code4life.entities.Project;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;

public class PerformanceTest {
	private static final int PERCENTILE = 60;
	private static final int THREAD_COUNT = 1;
	private static final int INVOCATION_COUNT = 1;
	@Rule
	public ContiPerfRule i = new ContiPerfRule();

	@BeforeClass
	public static void waitForVisualVM() {
		System.err.println("Waiting for VisualVM on this breakpoint");
	}

	@PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	@Required(percentile99 = PERCENTILE)
	@Test
	@Ignore
	public void can_compute_at__1495024446948() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("SAMPLES", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("MOLECULES", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s = new ArrayList<>();
		final List<Project> c = new ArrayList<>();
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(0, 0, 3, 3, 3));
		c.add(new Project(3, 3, 0, 0, 3));
		final Playfield p = new Playfield(1);
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(5, 5, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty();
	}

	@PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	@Required(percentile99 = PERCENTILE)
	@Test
	@Ignore
	public void can_compute_at__1495027151691() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("DIAGNOSIS", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("MOLECULES", 0, 0, 5, 1, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(0, 0, 1, "null", -1, -1, -1, -1, -1, -1));
		s.add(new Sample(1, 0, 1, "null", -1, -1, -1, -1, -1, -1));
		s.add(new Sample(2, 0, 1, "null", -1, -1, -1, -1, -1, -1));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(0, 0, 0, 4, 4));
		c.add(new Project(0, 0, 4, 4, 0));
		final Playfield p = new Playfield(1);
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(0, 4, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty();
	}

	@PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	@Required(percentile99=PERCENTILE)
	@Test
	@Ignore
	public void can_compute_at__1495027841472() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("DIAGNOSIS", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("MOLECULES", 0, 0, 5, 4, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(0, 0, 1, "E", 10, 0, 4, 0, 0, 0));
		s.add(new Sample(1, 0, 1, "C", 1, 1, 1, 0, 1, 2));
		s.add(new Sample(2, 0, 1, "A", 10, 0, 0, 4, 0, 0));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(0, 0, 4, 4, 0));
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(4, 4, 0, 0, 0));
		final Playfield p = new Playfield(1);
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(0, 1, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty();
	}
	@PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Ignore
	@Test public void can_compute_at__1495040058871() {
		final List<Robot> r=new ArrayList<>();
		r.add(new Robot("DIAGNOSIS",	0,	0,	0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("DIAGNOSIS",	0,	0,	0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s=new ArrayList<>();
		s.add(new Sample(0,	0,	1,	"A",	1,	0, 0, 0, 2, 1));
		s.add(new Sample(2,	0,	1,	"A",	1,	0, 1, 1, 1, 1));
		s.add(new Sample(4,	0,	1,	"A",	1,	0, 2, 2, 0, 1));
		s.add(new Sample(1,	1,	1,	"E",	1,	1, 1, 1, 1, 0));
		s.add(new Sample(3,	1,	1,	"A",	1,	0, 2, 0, 0, 2));
		s.add(new Sample(5,	1,	1,	"D",	1,	1, 0, 0, 1, 3));
		final List<Project> c=new ArrayList<>();
		c.add(new Project(3, 0, 0, 3, 3));
		c.add(new Project(3, 3, 3, 0, 0));
		c.add(new Project(4, 0, 0, 0, 4));
		final Playfield p = new Playfield(1);
		p.withRobots(r)
		.withSamples(s)
		.withProjects(c)
		.addAllAvailable(MoleculeStore.toMap(5, 5, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty();
	}
	@PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_at__1495040865116() {
		final List<Robot> r=new ArrayList<>();
		r.add(new Robot("MOLECULES",	0,	0,	0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("LABORATORY",	0,	1,	3, 2, 0, 0, 2, 0, 0, 1, 0, 0));
		final List<Sample> s=new ArrayList<>();
		s.add(new Sample(4,	0,	1,	"B",	1,	0, 1, 3, 1, 0));
		s.add(new Sample(3,	1,	1,	"A",	1,	0, 2, 0, 0, 2));
		s.add(new Sample(5,	1,	1,	"C",	1,	2, 1, 0, 0, 0));
		s.add(new Sample(0,	-1,	1,	"B",	10,	0, 0, 0, 4, 0));
		s.add(new Sample(2,	-1,	1,	"D",	1,	3, 0, 0, 0, 0));
		final List<Project> c=new ArrayList<>();
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(0, 0, 4, 4, 0));
		c.add(new Project(0, 0, 0, 4, 4));
		final Playfield p = new Playfield(1);
		p.withRobots(r)
		.withSamples(s)
		.withProjects(c)
		.addAllAvailable(MoleculeStore.toMap(2, 3, 5, 5, 3));
		assertThat(p.computeMoves()).isNotEmpty();
	}
}
