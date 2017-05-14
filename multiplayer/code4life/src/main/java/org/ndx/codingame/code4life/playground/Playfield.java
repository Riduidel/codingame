package org.ndx.codingame.code4life.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ndx.codingame.code4life.Constants;
import org.ndx.codingame.code4life.actions.ConnectToDiagnostic;
import org.ndx.codingame.code4life.actions.ConnectToDistribution;
import org.ndx.codingame.code4life.actions.ConnectToLaboratory;
import org.ndx.codingame.code4life.actions.ConnectToSampler;
import org.ndx.codingame.code4life.actions.Goto;
import org.ndx.codingame.code4life.actions.Wait;
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

	public Map<Molecule, Integer> getAvailable() {
		return available;
	}

	@Override
	public StringBuilder build() {
		final StringBuilder returned = new StringBuilder();
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, robots, List.class,
				Robot.class, "robots"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, samples, List.class,
				Sample.class, "samples"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield playfield = new Playfield();\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllRobots(robots);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllSamples(samples);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.setAvailable(Molecule.toMap(")
				.append(Molecule.moleculeMapToArguments(available)).append("));\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
				.append("assertThat(playfield.computeMoves()).isNotEmpty();\n");
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
		if (my.eta > 0) {
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
	 * @param indexOf
	 *            Passer 0 pour mes samples, 1 pour ceux de l'ennemi, et -1 pour
	 *            ceux dans le cloud
	 * @return
	 */
	private List<Sample> getSamplesIn(final int indexOf) {
		return samples.stream().filter((s) -> s.owner == indexOf).collect(Collectors.toList());
	}

	/**
	 * TODO refilter according to available molecules
	 *
	 * @return
	 */
	public List<Sample> getProcessableSamplesInCloud() {
		return getSamplesIn(-1).stream().filter((s) -> isProcessable(s)).collect(Collectors.toList());
	}

	private boolean isProcessable(final Sample s) {
		for (final Molecule m : Molecule.values()) {
			if (available.get(m) < s.cost.get(m)) {
				return false;
			}
		}
		return true;
	}

	public Action computeMoveOnDiagnosis(final Robot my, final List<Sample> mySamples) {
		// Do we have any unanalyzed sample ?
		// If so, publish it on cloud
		final List<Sample> unanalyzed = mySamples.stream().filter((s) -> !s.isDiagnosed()).collect(Collectors.toList());
		if (unanalyzed.isEmpty()) {
			// No unanalyzed sample ? cool
			// wait ... do we have enough sample ?
			if (mySamples.size() < Constants.MAX_SAMPLES) {
				final Optional<Sample> bestSample = my.findBestSampleIn(getProcessableSamplesInCloud());
				if (bestSample.isPresent()) {
					return new ConnectToDiagnostic(bestSample.get());
				} else {
					if (mySamples.isEmpty()) {
						return new Goto(Module.SAMPLES);
					} else {
						// We have not filled our collection, but can't fill it
						// any more, so give up
						// and jump on molecules
						return new Goto(Module.MOLECULES);
					}
				}
				// TODO can all owned samples be processed ? If not, release
				// sample
			} else {
				// What are we waiting ? Jump to molecules ! (TODO unless we
				// have enough molecules)
				return new Goto(Module.MOLECULES);
			}
		} else {
			return new ConnectToDiagnostic(unanalyzed.get(0));
		}
	}

	public Action computeMoveOnLaboratory(final Robot my, final List<Sample> mySamples) {
		final List<Sample> byHealth = mySamples.stream().sorted(Comparator.comparingInt(Sample::getHealth))
				.collect(Collectors.toList());
		if (byHealth.isEmpty()) {
			// Are there any samples in diagnosis ?
			if (getProcessableSamplesInCloud().isEmpty()) {
				return new Goto(Module.SAMPLES);
			} else {
				return new Goto(Module.DIAGNOSIS);
			}
		} else {
			// Do we have enough molecules to process first sample ?
			for (final Sample sample : byHealth) {
				if (my.canSendToLaboratory(sample)) {
					return new ConnectToLaboratory(sample);
				}
			}
			// TODO check if we can just reload molecules
			return new Goto(Module.DIAGNOSIS);
		}
	}

	public Action computeMoveOnMolecules(final Robot my, final List<Sample> mySamples) {
		if (!my.isFull()) {
			final List<Sample> byHealth = mySamples.stream().sorted(Comparator.comparingInt(Sample::getHealth))
					.collect(Collectors.toList());
			final EnumMap<Molecule, Integer> remaining = new EnumMap<>(my.counts);
			for (final Sample s : byHealth) {
				final Map<Molecule, Integer> missing = my.findMissingFor(s);
				if (!missing.isEmpty()) {
					final Molecule molecule = missing.keySet().iterator().next();
					if (getAvailable().get(molecule) > 0) {
						return new ConnectToDistribution(molecule);
					}
				}
			}
		}
		return new Goto(Module.LABORATORY);
	}

	public Action computeMoveOnSamples(final Robot my, final List<Sample> mySamples) {
		if (mySamples.size() < Constants.MAX_SAMPLES) {
			// Get one sample of the most interesting type
			// TODO compute type dynamically
			return new ConnectToSampler(1);
		} else {
			return new Goto(Module.DIAGNOSIS);
		}
	}
}
