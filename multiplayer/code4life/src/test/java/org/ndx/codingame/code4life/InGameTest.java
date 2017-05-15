package org.ndx.codingame.code4life;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.ndx.codingame.code4life.entities.MoleculeStore;
import org.ndx.codingame.code4life.entities.Project;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;

public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_compute_at__1494772413815() {
		final List<Robot> robots=new ArrayList<>();
			robots.add(new Robot("MOLECULES",	0,	0,	0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
			robots.add(new Robot("SAMPLES",	0,	0,	6, 4, 0, 0, 0, 0, 0, 0, 0, 0));
		final List<Sample> samples=new ArrayList<>();
			samples.add(new Sample(0,	0,	1,	"E",	1,	0, 0, 2, 1, 0));
			samples.add(new Sample(1,	0,	1,	"C",	1,	2, 1, 0, 0, 0));
			samples.add(new Sample(2,	0,	1,	"C",	1,	0, 2, 0, 2, 0));
			samples.add(new Sample(7,	1,	1,	"0",	-1,	-1, -1, -1, -1, -1));
			samples.add(new Sample(8,	1,	1,	"0",	-1,	-1, -1, -1, -1, -1));
			samples.add(new Sample(9,	1,	1,	"0",	-1,	-1, -1, -1, -1, -1));
			samples.add(new Sample(3,	-1,	0,	"E",	1,	0, 0, 1, 3, 1));
			samples.add(new Sample(4,	-1,	0,	"E",	1,	2, 2, 0, 1, 0));
			samples.add(new Sample(5,	-1,	0,	"C",	1,	1, 1, 0, 1, 2));
			samples.add(new Sample(6,	-1,	0,	"D",	1,	1, 1, 1, 0, 1));
		final Playfield playfield = new Playfield();
		playfield.addAllRobots(robots);
		playfield.addAllSamples(samples);
		playfield.addAllAvailable(MoleculeStore.toMap(0, 2, 6, 6, 6));
		assertThat(playfield.computeMoves())
			.isNotEqualTo("GOTO LABORATORY")
			.isNotEmpty();
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void should_release_not_processable_sample_stored_locally() {
		final List<Robot> robots=new ArrayList<>();
			robots.add(new Robot("DIAGNOSIS",	0,	3,	0, 1, 1, 0, 0, 1, 1, 1, 0, 0));
			robots.add(new Robot("DIAGNOSIS",	3,	31,	6, 3, 0, 0, 1, 0, 0, 0, 1, 1));
		final List<Sample> samples=new ArrayList<>();
			samples.add(new Sample(8,	0,	1,	"C",	1,	2, 1, 0, 0, 0));
			samples.add(new Sample(5,	1,	3,	"B",	40,	6, 3, 0, 0, 3));
			samples.add(new Sample(6,	-1,	3,	"E",	30,	3, 3, 5, 3, 0));
			samples.add(new Sample(7,	-1,	1,	"D",	1,	2, 0, 0, 2, 0));
		final List<Project> projects=new ArrayList<>();
			projects.add(new Project(4, 0, 0, 0, 4));
			projects.add(new Project(0, 0, 4, 4, 0));
			projects.add(new Project(3, 3, 3, 0, 0));
		final Playfield playfield = new Playfield();
		playfield.addAllRobots(robots);
		playfield.addAllSamples(samples);
		playfield.addAllProjects(projects);
		playfield.addAllAvailable(MoleculeStore.toMap(0, 2, 5, 6, 5));
		assertThat(playfield.computeMoves()).isEqualTo("CONNECT 8").isNotEmpty();
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void should_not_take_sample_8() {
		final List<Robot> robots=new ArrayList<>();
			robots.add(new Robot("DIAGNOSIS",	0,	3,	0, 1, 1, 0, 0, 1, 1, 1, 0, 0));
			robots.add(new Robot("DIAGNOSIS",	3,	31,	6, 3, 0, 0, 1, 0, 0, 0, 1, 1));
		final List<Sample> samples=new ArrayList<>();
			samples.add(new Sample(8,	-1,	1,	"C",	1,	2, 1, 0, 0, 0));
			samples.add(new Sample(5,	1,	3,	"B",	40,	6, 3, 0, 0, 3));
			samples.add(new Sample(6,	-1,	3,	"E",	30,	3, 3, 5, 3, 0));
			samples.add(new Sample(7,	-1,	1,	"D",	1,	2, 0, 0, 2, 0));
		final List<Project> projects=new ArrayList<>();
			projects.add(new Project(4, 0, 0, 0, 4));
			projects.add(new Project(0, 0, 4, 4, 0));
			projects.add(new Project(3, 3, 3, 0, 0));
		final Playfield playfield = new Playfield();
		playfield.addAllRobots(robots);
		playfield.addAllSamples(samples);
		playfield.addAllProjects(projects);
		playfield.addAllAvailable(MoleculeStore.toMap(0, 2, 5, 6, 5));
		assertThat(playfield.computeMoves()).isNotEqualTo("CONNECT 8").isNotEmpty();
	}
}
