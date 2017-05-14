package org.ndx.codingame.code4life.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ndx.codingame.code4life.actions.ConnectToDiagnostic;
import org.ndx.codingame.code4life.actions.ConnectToDistribution;
import org.ndx.codingame.code4life.actions.ConnectToLaboratory;
import org.ndx.codingame.code4life.actions.ConnectToSampler;
import org.ndx.codingame.code4life.actions.Goto;
import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.Molecule;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;

public class Playfield implements ToUnitTestFiller {
	private final List<Robot> robots = new ArrayList<>();
	private final List<Sample> samples = new ArrayList<>();
	private Map<Molecule, Integer> available;

	@Override
	public StringBuilder build() {
		final StringBuilder returned = new StringBuilder();
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, robots, List.class, Robot.class, "robots"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, samples, List.class, Sample.class, "samples"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield playfield = new Playfield();\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllRobots(robots);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllSamples(samples);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
			.append("playfield.setAvailable(Molecule.toMap(")
			.append(Molecule.moleculeMapToArguments(available))
			.append("));\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("assertThat(playfield.computeMoves()).isNotEmpty();\n");
		return returned;
	}

	public void addAllRobots(final List<Robot> robots) {
		this.robots.addAll(robots);
	}

	public void setAvailable(final Map<Molecule, Integer> available) {
		this.available = available;
	}

	public void addAllSamples(final Collection<Sample> samples) {
		this.samples.addAll(samples);
	}


	public String computeMoves() {
		final Robot my = robots.get(0);
		return computeMovesOf(my, samples).toCommandString();
	}

	private static Action computeMovesOf(final Robot my, final Collection<Sample> samples) {
		final List<Sample> onRobot = samples.stream().filter((s) -> s.owner == 0).collect(Collectors.toList());
		if(onRobot.isEmpty()) {
			System.err.println("Robot has no sample");
			if(my.target.equals(Module.SAMPLES)) {
				System.err.println("Getting samples from cloud");
				return new ConnectToSampler(2);
			} else {
				System.err.println("Moving to samples");
				// Robot has no sample. Go to Diagnosis
				return new Goto(Module.SAMPLES);
			}
		} else {
			System.err.println("Having samples");
			// Just take first sample as reference
			final Sample toProcess = onRobot.get(0);
			if(toProcess.isDiagnosed()) {
				if(my.target.equals(Module.MOLECULES)) {
					final List<Molecule> required = my.findRequiredMoleculesFor(toProcess);
					if(required.isEmpty()) {
						return new Goto(Module.LABORATORY);
					} else {
						return new ConnectToDistribution(required.get(0));
					}
				} else if(my.target.equals(Module.LABORATORY)) {
					return new ConnectToLaboratory(toProcess);
				} else {
					return new Goto(Module.MOLECULES);
				}
			} else {
				if(my.target.equals(Module.DIAGNOSIS)) {
					return new ConnectToDiagnostic(toProcess);
				} else {
					return new Goto(Module.DIAGNOSIS);
				}
			}
		}
	}

}
