package org.ndx.codingame.code4life;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.ndx.codingame.code4life.entities.MoleculeStore;
import org.ndx.codingame.code4life.entities.Project;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;

public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1494772413815() {
		final List<Robot> robots = new ArrayList<>();
		robots.add(new Robot("MOLECULES", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		robots.add(new Robot("SAMPLES", 0, 0, 6, 4, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> samples = new ArrayList<>();
		samples.add(new Sample(0, 0, 1, "E", 1, 0, 0, 2, 1, 0));
		samples.add(new Sample(1, 0, 1, "C", 1, 2, 1, 0, 0, 0));
		samples.add(new Sample(2, 0, 1, "C", 1, 0, 2, 0, 2, 0));
		samples.add(new Sample(7, 1, 1, "0", -1, -1, -1, -1, -1, -1));
		samples.add(new Sample(8, 1, 1, "0", -1, -1, -1, -1, -1, -1));
		samples.add(new Sample(9, 1, 1, "0", -1, -1, -1, -1, -1, -1));
		samples.add(new Sample(3, -1, 0, "E", 1, 0, 0, 1, 3, 1));
		samples.add(new Sample(4, -1, 0, "E", 1, 2, 2, 0, 1, 0));
		samples.add(new Sample(5, -1, 0, "C", 1, 1, 1, 0, 1, 2));
		samples.add(new Sample(6, -1, 0, "D", 1, 1, 1, 1, 0, 1));
		final Playfield playfield = new Playfield();
		playfield.withRobots(robots);
		playfield.withSamples(samples);
		playfield.addAllAvailable(MoleculeStore.toMap(0, 2, 6, 6, 6));
		assertThat(playfield.computeMoves()).isNotEqualTo("GOTO LABORATORY").isNotEmpty();
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void should_release_not_processable_sample_stored_locally() {
		final List<Robot> robots = new ArrayList<>();
		robots.add(new Robot("DIAGNOSIS", 0, 3, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0));
		robots.add(new Robot("DIAGNOSIS", 3, 31, 6, 3, 0, 0, 1, 0, 0, 0, 1, 1));
		final List<Sample> samples = new ArrayList<>();
		samples.add(new Sample(8, 0, 1, "C", 1, 2, 1, 0, 0, 0));
		samples.add(new Sample(5, 1, 3, "B", 40, 6, 3, 0, 0, 3));
		samples.add(new Sample(6, -1, 3, "E", 30, 3, 3, 5, 3, 0));
		samples.add(new Sample(7, -1, 1, "D", 1, 2, 0, 0, 2, 0));
		final List<Project> projects = new ArrayList<>();
		projects.add(new Project(4, 0, 0, 0, 4));
		projects.add(new Project(0, 0, 4, 4, 0));
		projects.add(new Project(3, 3, 3, 0, 0));
		final Playfield playfield = new Playfield();
		playfield.withRobots(robots);
		playfield.withSamples(samples);
		playfield.withProjects(projects);
		playfield.addAllAvailable(MoleculeStore.toMap(0, 2, 5, 6, 5));
		assertThat(playfield.computeMoves()).isEqualTo("CONNECT 8").isNotEmpty();
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void should_not_take_sample_8() {
		final List<Robot> robots = new ArrayList<>();
		robots.add(new Robot("DIAGNOSIS", 0, 3, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0));
		robots.add(new Robot("DIAGNOSIS", 3, 31, 6, 3, 0, 0, 1, 0, 0, 0, 1, 1));
		final List<Sample> samples = new ArrayList<>();
		samples.add(new Sample(8, -1, 1, "C", 1, 2, 1, 0, 0, 0));
		samples.add(new Sample(5, 1, 3, "B", 40, 6, 3, 0, 0, 3));
		samples.add(new Sample(6, -1, 3, "E", 30, 3, 3, 5, 3, 0));
		samples.add(new Sample(7, -1, 1, "D", 1, 2, 0, 0, 2, 0));
		final List<Project> projects = new ArrayList<>();
		projects.add(new Project(4, 0, 0, 0, 4));
		projects.add(new Project(0, 0, 4, 4, 0));
		projects.add(new Project(3, 3, 3, 0, 0));
		final Playfield playfield = new Playfield();
		playfield.withRobots(robots);
		playfield.withSamples(samples);
		playfield.withProjects(projects);
		playfield.addAllAvailable(MoleculeStore.toMap(0, 2, 5, 6, 5));
		assertThat(playfield.computeMoves()).isNotEqualTo("CONNECT 8").isNotEmpty();
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1494859419190_should_go_to_samples() {
		final List<Robot> robots = new ArrayList<>();
		robots.add(new Robot("LABORATORY", 0, 3, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0));
		robots.add(new Robot("MOLECULES", 1, 40, 0, 3, 0, 0, 0, 0, 0, 0, 1, 1));
		final List<Sample> samples = new ArrayList<>();
		samples.add(new Sample(3, 1, 2, "D", 10, 0, 3, 0, 2, 3));
		final List<Project> projects = new ArrayList<>();
		projects.add(new Project(0, 0, 4, 4, 0));
		projects.add(new Project(4, 0, 0, 0, 4));
		projects.add(new Project(3, 3, 0, 0, 3));
		final Playfield playfield = new Playfield();
		playfield.withRobots(robots);
		playfield.withSamples(samples);
		playfield.withProjects(projects);
		playfield.addAllAvailable(MoleculeStore.toMap(6, 3, 6, 5, 6));
		assertThat(playfield.computeMoves()).isNotEmpty().isEqualTo("GOTO SAMPLES");
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1495026032051_keep_that_sample_and_go() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("DIAGNOSIS", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("SAMPLES", 0, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(1, 0, 1, "A", 10, 0, 0, 4, 0, 0));
		s.add(new Sample(0, -1, 1, "A", 1, 0, 2, 2, 0, 1));
		s.add(new Sample(2, -1, 1, "E", 1, 2, 2, 0, 1, 0));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(3, 3, 0, 0, 3));
		c.add(new Project(3, 3, 3, 0, 0));
		c.add(new Project(0, 0, 0, 4, 4));
		final Playfield p = new Playfield();
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(0, 0, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty().isNotEqualTo("CONNECT 1");
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1495029553227_should_not_juggle_with_samples() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("DIAGNOSIS", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("SAMPLES", 2, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(0, 0, 1, "C", 1, 0, 0, 0, 3, 0));
		s.add(new Sample(1, -1, 1, "D", 1, 3, 0, 0, 0, 0));
		s.add(new Sample(2, -1, 1, "C", 1, 1, 1, 0, 1, 2));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(4, 4, 0, 0, 0));
		c.add(new Project(3, 0, 0, 3, 3));
		c.add(new Project(3, 3, 3, 0, 0));
		final Playfield p = new Playfield();
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(0, 0, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty().isEqualTo("GOTO MOLECULES");
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1495030003532_get_the_right_molecule_type() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("MOLECULES", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("SAMPLES", 0, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(2, 0, 1, "A", 10, 0, 0, 4, 0, 0));
		s.add(new Sample(3, 1, 1, "null", -1, -1, -1, -1, -1, -1));
		s.add(new Sample(1, -1, 1, "E", 1, 2, 0, 2, 0, 0));
		s.add(new Sample(0, -1, 1, "B", 1, 0, 1, 3, 1, 0));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(0, 4, 4, 0, 0));
		c.add(new Project(3, 3, 3, 0, 0));
		c.add(new Project(0, 0, 3, 3, 3));
		final Playfield p = new Playfield();
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(0, 0, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty().isNotEqualTo("CONNECT 2");
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1495032565046_drop_sample_on_laboratory() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("LABORATORY", 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("DIAGNOSIS", 0, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(2, 0, 1, "A", 10, 0, 0, 4, 0, 0));
		s.add(new Sample(4, 1, 1, "null", -1, -1, -1, -1, -1, -1));
		s.add(new Sample(5, 1, 1, "null", -1, -1, -1, -1, -1, -1));
		s.add(new Sample(1, -1, 1, "E", 1, 2, 0, 2, 0, 0));
		s.add(new Sample(0, -1, 1, "B", 1, 0, 1, 3, 1, 0));
		s.add(new Sample(3, -1, 1, "D", 1, 0, 2, 1, 0, 0));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(0, 4, 4, 0, 0));
		c.add(new Project(3, 3, 3, 0, 0));
		c.add(new Project(0, 0, 3, 3, 3));
		final Playfield p = new Playfield();
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(0, 0, 1, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty().isNotEqualTo("GOTO DIAGNOSIS");
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1495033167605_do_not_go_to_molecules_with_hands_empty() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("DIAGNOSIS", 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0));
		r.add(new Robot("SAMPLES", 1, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(10, 1, 1, "null", -1, -1, -1, -1, -1, -1));
		s.add(new Sample(11, 1, 1, "null", -1, -1, -1, -1, -1, -1));
		s.add(new Sample(0, -1, 1, "A", 1, 0, 2, 0, 0, 2));
		s.add(new Sample(1, -1, 1, "D", 1, 2, 0, 0, 2, 0));
		s.add(new Sample(3, -1, 1, "E", 1, 2, 0, 2, 0, 0));
		s.add(new Sample(4, -1, 1, "A", 1, 0, 1, 1, 1, 1));
		s.add(new Sample(7, -1, 1, "D", 10, 4, 0, 0, 0, 0));
		s.add(new Sample(9, -1, 1, "A", 1, 0, 3, 0, 0, 0));
		s.add(new Sample(5, -1, 1, "C", 1, 1, 1, 0, 1, 1));
		s.add(new Sample(6, -1, 1, "B", 1, 1, 0, 1, 2, 1));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(4, 4, 0, 0, 0));
		c.add(new Project(4, 0, 0, 0, 4));
		final Playfield p = new Playfield();
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(0, 0, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty().isNotEqualTo("GOTO MOLECULES");
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1495035890753_can_move() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("DIAGNOSIS", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		r.add(new Robot("MOLECULES", 0, 0, 5, 4, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(0, 0, 1, "A", 1, 0, 0, 0, 2, 1));
		s.add(new Sample(1, 0, 1, "E", 1, 1, 1, 1, 1, 0));
		s.add(new Sample(2, 0, 1, "A", 1, 0, 1, 1, 1, 1));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(3, 0, 0, 3, 3));
		c.add(new Project(3, 3, 3, 0, 0));
		c.add(new Project(4, 0, 0, 0, 4));
		final Playfield p = new Playfield();
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(0, 1, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty();
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1495044797957_do_not_release_sample_8() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("DIAGNOSIS", 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0));
		r.add(new Robot("SAMPLES", 0, 14, 0, 0, 0, 0, 0, 1, 1, 2, 1, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(6, 0, 1, "A", 1, 0, 1, 2, 1, 1));
		s.add(new Sample(8, 0, 1, "C", 1, 0, 2, 0, 2, 0));
		s.add(new Sample(9, 1, 1, "null", -1, -1, -1, -1, -1, -1));
		s.add(new Sample(10, 1, 1, "null", -1, -1, -1, -1, -1, -1));
		s.add(new Sample(7, -1, 1, "A", 1, 0, 0, 0, 2, 1));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(0, 0, 4, 4, 0));
		c.add(new Project(0, 0, 0, 4, 4));
		final Playfield p = new Playfield();
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(5, 5, 5, 5, 5));
		assertThat(p.computeMoves()).isNotEmpty().isNotEqualTo("CONNECT 8");
	}

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void can_compute_at__1495045436336() {
		final List<Robot> r = new ArrayList<>();
		r.add(new Robot("LABORATORY", 0, 12, 5, 0, 4, 1, 0, 0, 1, 0, 1, 1));
		r.add(new Robot("LABORATORY", 1, 3, 0, 0, 1, 2, 0, 1, 0, 1, 1, 0));
		final List<Sample> s = new ArrayList<>();
		s.add(new Sample(6, 0, 3, "B", 30, 3, 0, 3, 3, 5));
		s.add(new Sample(7, 0, 1, "D", 10, 4, 0, 0, 0, 0));
		s.add(new Sample(8, 0, 1, "B", 1, 1, 0, 1, 1, 1));
		s.add(new Sample(9, 1, 1, "A", 1, 0, 0, 0, 2, 1));
		s.add(new Sample(10, 1, 1, "E", 1, 0, 0, 2, 1, 0));
		s.add(new Sample(11, 1, 1, "E", 1, 0, 0, 1, 3, 1));
		final List<Project> c = new ArrayList<>();
		c.add(new Project(4, 4, 0, 0, 0));
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(0, 4, 4, 0, 0));
		final Playfield p = new Playfield();
		p.withRobots(r).withSamples(s).withProjects(c).addAllAvailable(MoleculeStore.toMap(0, 5, 0, 2, 5));
		assertThat(p.computeMoves()).isNotEmpty();
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_at__1495049035913_do_not_go_back_to_diagnosis() {
		final List<Robot> r=new ArrayList<>();
		r.add(new Robot("MOLECULES",	0,	12,	1, 0, 3, 2, 4, 0, 1, 0, 1, 1));
		r.add(new Robot("SAMPLES",	0,	35,	0, 0, 0, 0, 0, 3, 1, 1, 2, 1));
		final List<Sample> s=new ArrayList<>();
		s.add(new Sample(9,	0,	1,	"A",	1,	0, 0, 0, 2, 1));
		s.add(new Sample(10,	0,	1,	"E",	1,	0, 0, 2, 1, 0));
		s.add(new Sample(8,	0,	3,	"B",	30,	3, 0, 3, 3, 5));
		s.add(new Sample(14,	1,	2,	"E",	20,	0, 0, 0, 0, 5));
		s.add(new Sample(15,	1,	2,	"null",	-1,	-1, -1, -1, -1, -1));
		s.add(new Sample(16,	1,	2,	"null",	-1,	-1, -1, -1, -1, -1));
		final List<Project> c=new ArrayList<>();
		c.add(new Project(4, 4, 0, 0, 0));
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(0, 4, 4, 0, 0));
		final Playfield p = new Playfield();
		p.withRobots(r)
		.withSamples(s)
		.withProjects(c)
		.addAllAvailable(MoleculeStore.toMap(4, 5, 2, 3, 1));
		assertThat(p.computeMoves()).isNotEmpty().isNotEqualTo("GOTO DIAGNOSIS");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_at__1495090780820() {
		final List<Robot> r=new ArrayList<>();
		r.add(new Robot("MOLECULES",	0,	47,	1, 0, 0, 0, 0, 1, 1, 3, 2, 2));
		r.add(new Robot("LABORATORY",	2,	66,	4, 1, 1, 1, 1, 2, 2, 1, 2, 2));
		final List<Sample> s=new ArrayList<>();
		s.add(new Sample(17,	0,	2,	"C",	10,	3, 0, 2, 3, 0));
		s.add(new Sample(21,	0,	2,	"E",	10,	3, 2, 2, 0, 0));
		s.add(new Sample(20,	1,	2,	"B",	20,	5, 3, 0, 0, 0));
		s.add(new Sample(16,	1,	2,	"A",	10,	2, 3, 0, 3, 0));
		final List<Project> c=new ArrayList<>();
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(4, 4, 0, 0, 0));
		c.add(new Project(3, 3, 0, 0, 3));
		final Playfield p = new Playfield();
		p.withRobots(r)
		.withSamples(s)
		.withProjects(c)
		.addAllAvailable(MoleculeStore.toMap(0, 4, 4, 4, 4));
		assertThat(p.computeMoves()).isNotEmpty().isNotEqualTo("CONNECT 17");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Ignore
	@Test public void can_compute_at__1495132276135_should_go_to_samples() {
		final List<Robot> r=new ArrayList<>();
		r.add(new Robot("LABORATORY",	0,	59,	0, 0, 0, 0, 0, 3, 2, 2, 2, 3));
		r.add(new Robot("DIAGNOSIS",	1,	76,	1, 1, 2, 1, 1, 2, 2, 3, 2, 0));
		final List<Sample> s=new ArrayList<>();
		s.add(new Sample(12,	1,	3,	"A",	50,	3, 0, 0, 0, 7));
		s.add(new Sample(23,	1,	3,	"null",	-1,	-1, -1, -1, -1, -1));
		s.add(new Sample(24,	1,	3,	"null",	-1,	-1, -1, -1, -1, -1));
		s.add(new Sample(21,	-1,	3,	"A",	30,	0, 3, 3, 5, 3));
		final List<Project> c=new ArrayList<>();
		c.add(new Project(4, 0, 0, 0, 4));
		c.add(new Project(3, 3, 3, 0, 0));
		c.add(new Project(0, 3, 3, 3, 0));
		final Playfield p = new Playfield();
		p.withRobots(r)
		.withSamples(s)
		.withProjects(c)
		.addAllAvailable(MoleculeStore.toMap(4, 4, 3, 4, 4));
		assertThat(p.computeMoves()).isNotEqualTo("GOTO DIAGNOSIS");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_at__1495219580823_should_not_take_sample_9_as_it_requires_too_much_B_molecules() {
		final List<Robot> r=new ArrayList<>();
		r.add(new Robot("DIAGNOSIS",	0,	16,	3, 0, 2, 3, 0, 1, 2, 1, 2, 1));
		r.add(new Robot("SAMPLES",	0,	18,	1, 0, 0, 2, 1, 0, 2, 2, 2, 3));
		final List<Sample> s=new ArrayList<>();
		s.add(new Sample(18,	1,	2,	"null",	-1,	-1, -1, -1, -1, -1));
		s.add(new Sample(19,	1,	2,	"null",	-1,	-1, -1, -1, -1, -1));
		s.add(new Sample(9,	-1,	3,	"C",	50,	0, 7, 3, 0, 0));
		s.add(new Sample(12,	-1,	3,	"A",	40,	0, 0, 0, 0, 7));
		final List<Project> c=new ArrayList<>();
		c.add(new Project(0, 3, 3, 3, 0));
		c.add(new Project(3, 0, 0, 3, 3));
		c.add(new Project(0, 0, 3, 3, 3));
		final Playfield p = new Playfield();
		p.withRobots(r)
		.withSamples(s)
		.withProjects(c)
		.addAllAvailable(MoleculeStore.toMap(1, 5, 3, 0, 4));
		assertThat(p.computeMoves()).isNotEqualTo("CONNECT 9");
	}

}
