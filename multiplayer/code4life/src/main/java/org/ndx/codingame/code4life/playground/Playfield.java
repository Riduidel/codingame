package org.ndx.codingame.code4life.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ndx.codingame.code4life.actions.Wait;
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

	public Map<Molecule, Integer> getAvailable() {
		return available;
	}

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
		return computeMoveOf(my).toCommandString();
	}

	private Action computeMoveOf(final Robot my) {
		if(my.eta>0) {
			// As long as we are not on a module, move to that module
			return new Wait();
		} else {
			return my.target.computeMoveOf(my, this);
		}
	}

	public List<Sample> getSamplesOf(final Robot my) {
		return getSamplesIn(robots.indexOf(my));
	}

	/**
	 *
	 * @param indexOf Passer 0 pour mes samples, 1 pour ceux de l'ennemi, et -1 pour ceux dans le cloud
	 * @return
	 */
	private List<Sample> getSamplesIn(final int indexOf) {
		return samples.stream()
				.filter((s) -> s.owner==indexOf)
				.collect(Collectors.toList());
	}

	/**
	 * TODO refilter according to available molecules
	 * @return
	 */
	public List<Sample> getProcessableSamplesInCloud() {
		return getSamplesIn(-1).stream()
				.filter((s) -> isProcessable(s))
				.collect(Collectors.toList());
	}

	private boolean isProcessable(final Sample s) {
		for(final Molecule m : Molecule.values()) {
			if(available.get(m)<s.cost.get(m)) {
				return false;
			}
		}
		return true;
	}
}
