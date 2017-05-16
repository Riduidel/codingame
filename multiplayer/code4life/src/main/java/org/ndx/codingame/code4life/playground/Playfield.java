package org.ndx.codingame.code4life.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import org.ndx.codingame.code4life.entities.MoleculeStore;
import org.ndx.codingame.code4life.entities.Project;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;

public class Playfield extends MoleculeStore implements ToUnitTestFiller {
	private final List<Robot> robots = new ArrayList<>();
	private final List<Sample> samples = new ArrayList<>();
	private final List<Project> projects = new ArrayList<>();
	private List<Sample> usableSamples;

	public void addAllProjects(final List<Project> projects) {
		this.projects.addAll(projects);
	}

	public void addAllRobots(final List<Robot> robots) {
		this.robots.addAll(robots);
	}

	public void addAllSamples(final Collection<Sample> samples) {
		this.samples.addAll(samples);
	}

	@Override
	public StringBuilder build() {
		final StringBuilder returned = new StringBuilder();
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, robots, List.class,
				Robot.class, "robots"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, samples, List.class,
				Sample.class, "samples"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, projects, List.class,
				Project.class, "projects"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield playfield = new Playfield();\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllRobots(robots);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllSamples(samples);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllProjects(projects);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllAvailable(MoleculeStore.toMap(")
				.append(MoleculeStore.moleculeMapToArguments(getAvailable())).append("));\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
				.append("assertThat(playfield.computeMoves()).isNotEmpty();\n");
		return returned;
	}

	private Action computeMoveOf(final Robot my) {
		if (my.eta > 0) {
			// As long as we are not on a module, move to that module
			return new Wait();
		} else {
			return my.target.computeMoveOf(my, this);
		}
	}

	public Action computeMoveOnDiagnosis(final Robot my, final List<Sample> mySamples) {
		// Do we have any unanalyzed sample ?
		// If so, publish it on cloud
		final List<Sample> unanalyzed = mySamples.stream().filter((s) -> !s.isDiagnosed()).collect(Collectors.toList());
		if (unanalyzed.isEmpty()) {
			// No unanalyzed sample ? cool
			// wait ... do we have enough sample ?
			if (mySamples.size()>0) {
				for(final Sample s : mySamples) {
					if(s.score(this, my)<0) {
						// Push sample in cloud
						return new ConnectToDiagnostic(s);
					}
				}
			}
			if (mySamples.size() < Constants.MAX_SAMPLES) {
				final Optional<Sample> bestSample = my.findBestSampleIn(getUsableSamplesOf(my));
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
	private List<Sample> getUsableSamplesOf(final Robot my) {
		if(usableSamples==null) {
			usableSamples = getSamplesOf(this).stream()
					.filter((s) -> s.score(this, my)>Constants.SCORE_NOT_PROCESSABLE)
					.collect(Collectors.toList());
		}
		return usableSamples;
	}

	public Action computeMoveOnLaboratory(final Robot my, final List<Sample> mySamples) {
		final List<Sample> byHealth = mySamples.stream().sorted(Comparator.comparingInt(Sample::getHealth))
				.collect(Collectors.toList());
		if (byHealth.isEmpty()) {
			// Are there any samples in diagnosis ?
			if(getUsableSamplesOf(my).isEmpty()) {
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
			for (final Sample sample : byHealth) {
				if(isProcessable(my, sample)) {
					return new Goto(Module.MOLECULES);
				}
			}
			// TODO check if we can just reload molecules
			return new Goto(Module.DIAGNOSIS);
		}
	}

	private boolean isProcessable(final Robot my, final Sample sample) {
		return findMissingFor(my, sample).isEmpty();
	}

	private Map<Molecule, Integer> findMissingFor(final Robot my, final Sample sample) {
		final Map<Molecule, Integer> missingFromRobot = my.findMissingFor(sample);
		final Map<Molecule, Integer> missingFromStore = findMissingFor(missingFromRobot);
		return missingFromStore;
	}

	public Action computeMoveOnMolecules(final Robot my, final List<Sample> mySamples) {
		if (!my.isFull()) {
			final List<Sample> byHealth = mySamples.stream().sorted(Comparator.comparingInt(Sample::getHealth))
					.collect(Collectors.toList());
			for (final Sample s : byHealth) {
				final Map<Molecule, Integer> missing = my.findMissingFor(s);
				if (!missing.isEmpty()) {
					final Molecule molecule = missing.keySet().iterator().next();
					if (getAvailable().get(molecule) > 0) {
						return new ConnectToDistribution(molecule);
					}
				}
			}
			// Take one molecule of the least owned one
			final Optional<Molecule> toGet = getAvailable().keySet().stream()
				.filter((m) -> getAvailable().get(m)>0)
				.sorted((first, second) -> (int) Math.signum(my.getAvailable().get(first)-my.getAvailable().get(second)))
				.findFirst();
			if(toGet.isPresent()) {
				return new ConnectToDistribution(toGet.get());
			}
		}
		for(final Sample s : getSamplesOf(my)) {
			if(my.canSendToLaboratory(s)) {
				return new Goto(Module.LABORATORY);
			}
		}
		return new Goto(Module.DIAGNOSIS);
	}

	public Action computeMoveOnSamples(final Robot my, final List<Sample> mySamples) {
		if (mySamples.size() < Constants.MAX_SAMPLES) {
			// Get one sample of the most interesting type
			int totalExpertise = MoleculeStore.totalCostOf(my.expertise);
			for(final Sample s : mySamples) {
				totalExpertise = totalExpertise - s.rank*Constants.RANK_FACTOR;
			}
			return new ConnectToSampler(Math.min(Math.max(totalExpertise, 1), 3));
		} else {
			return new Goto(Module.DIAGNOSIS);
		}
	}

	public String computeMoves() {
		final Robot my = robots.get(0);
		return computeMoveOf(my).toCommandString();
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

	public List<Sample> getSamplesOf(final MoleculeStore my) {
		/**
		 * maniac magic : as playfield is not in robots list, it returns -1, which is index for
		 * samples in cloud !
		 */
		return getSamplesIn(robots.indexOf(my));
	}
	public Set<Molecule> completableProjectsRequirements() {
		final Set<Molecule> returned = new HashSet<>();
		for(final Project p : completableProjects()) {
			returned.addAll(p.getAvailable().keySet());
		}
		return returned;
	}

	private Collection<Project> completableProjects() {
		return projects;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Playfield [");
		builder.append("getAvailable()=");
		builder.append(getAvailable());
		builder.append(",\nprojects=[");
		builder.append(projects.stream().map(Project::toString).collect(Collectors.joining(",\n\t", "\n\t", "]")));
		builder.append("]");
		return builder.toString();
	}
}
