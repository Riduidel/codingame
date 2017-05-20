package org.ndx.codingame.code4life.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.ndx.codingame.code4life.Constants;
import org.ndx.codingame.code4life.actions.Wait;
import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.Molecule;
import org.ndx.codingame.code4life.entities.MoleculeStore;
import org.ndx.codingame.code4life.entities.Project;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.states.Diagnosed;
import org.ndx.codingame.code4life.playground.states.Initial;
import org.ndx.codingame.code4life.playground.states.Known;
import org.ndx.codingame.code4life.playground.states.LoadedSamples;
import org.ndx.codingame.code4life.playground.states.NotEnough;
import org.ndx.codingame.code4life.playground.states.Processing;
import org.ndx.codingame.code4life.playground.states.Servicable;
import org.ndx.codingame.code4life.playground.states.StatedComputer;
import org.ndx.codingame.code4life.playground.states.Unknown;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;

public class Playfield extends MoleculeStore implements ToUnitTestFiller {
	protected final List<Robot> robots = new ArrayList<>();
	protected final List<Sample> samples = new ArrayList<>();
	protected final List<Project> projects = new ArrayList<>();
	private final Map<Integer, List<Sample>> samplesByOwner = new HashMap<>();

	public Playfield withProjects(final List<Project> projects) {
		this.projects.addAll(projects);
		return this;
	}

	public Playfield withRobots(final List<Robot> robots) {
		this.robots.addAll(robots);
		return this;
	}

	public Playfield withSamples(final Collection<Sample> samples) {
		this.samples.addAll(samples);
		return this;
	}

	@Override
	public StringBuilder build(final String effectiveCommand) {
		final StringBuilder returned = new StringBuilder();
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, robots, List.class,
				Robot.class, "r"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, samples, List.class,
				Sample.class, "s"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, projects, List.class,
				Project.class, "c"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield p = new Playfield();\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("p.withRobots(r)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t.withSamples(s)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t.withProjects(c)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t.addAllAvailable(MoleculeStore.toMap(")
		.append(MoleculeStore.moleculeMapToArguments(getAvailable())).append("));\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
		.append("assertThat(p.computeMoves()).isNotEqualTo(\"").append(effectiveCommand).append("\");\n");
		return returned;
	}

	private Action computeMoveOf(final Robot my) {
		if (my.eta == 0) {
			final State state = identifyState(my);
			final StatedComputer computer = getComputerFor(state);
			return computer.compute(my);
		}
		// As long as we are not on a module, move to that module
		return new Wait();
	}
	private StatedComputer getComputerFor(final State state) {
		switch(state) {
		case INITIAL:
			return new Initial(this);
		case NOT_ENOUGH_SAMPLES:
			return new NotEnough(this);
		case LOADED_SAMPLES:
			return new LoadedSamples(this);
		case UNKNOWN_SAMPLES:
			return new Unknown(this);
		case DIAGNOSED_SAMPLES:
			return new Diagnosed(this);
		case KNOWN_SAMPLES:
			return new Known(this);
		case SERVICABLE:
			return new Servicable(this);
		case PROCESSING:
			return new Processing(this);
		default:
			throw new UnsupportedOperationException("unknown state "+state);
		}
	}

	private State identifyState(final Robot my) {
		switch(my.target) {
		case START_POS:
			return State.INITIAL;
		case SAMPLES:
			if(isFullOfSamples(my)) {
				return State.LOADED_SAMPLES;
			} else {
				return State.NOT_ENOUGH_SAMPLES;
			}
		case DIAGNOSIS:
			if(hasUndiagnosedSamples(my)) {
				return State.UNKNOWN_SAMPLES;
			} else {
				return State.DIAGNOSED_SAMPLES;
			}
		case MOLECULES:
			if(canGetMoleculesForSamples(my)) {
				return State.SERVICABLE;
			} else {
				return State.KNOWN_SAMPLES;
			}
		case LABORATORY:
			return State.PROCESSING;
		default:
			throw new UnsupportedOperationException("unknown target "+my.target.name());
		}
	}

	/**
	 * @param my
	 * @return true if we can get molecules for at least one of the robots
	 */
	private boolean canGetMoleculesForSamples(final Robot my) {
		return !samplesWithMolecules(my).isEmpty();
	}

	public List<Sample> samplesWithoutMolecules(final Robot my) {
		return samplesWithMoleculesSetTo(my, false);
	}
	public List<Sample> samplesWithMolecules(final Robot my) {
		return samplesWithMoleculesSetTo(my, true);
	}
	public List<Sample> samplesWithMoleculesSetTo(final Robot my, final boolean expected) {
		final List<Sample> returned = new ArrayList<>();
		for(final Sample s: getSamplesListOf(my)) {
			if(canProvideMoleculesFor(my, s)==expected) {
				returned.add(s);
			}
		}
		return returned;
	}

	public boolean hasUndiagnosedSamples(final Robot my) {
		return getFirstUndiagnosedSample(my).isPresent();
	}
	public Optional<Sample> getFirstUndiagnosedSample(final Robot my) {
		for(final Sample s : getSamplesListOf(my)) {
			if(s.isDiagnosed()==false) {
				return Optional.of(s);
			}
		}
		return Optional.empty();
	}

	public boolean hasDiagnosedSamples(final Robot my) {
		return !getSamplesListOf(my).isEmpty() && !getFirstUndiagnosedSample(my).isPresent();
	}
	public boolean isFullOfDiagnosedSamples(final Robot my) {
		if(!isFullOfSamples(my)) {
			return false;
		}
		for(final Sample s : getSamplesListOf(my)) {
			if(s.isDiagnosed()==false) {
				return false;
			}
		}
		return true;
	}

	public boolean isFullOfSamples(final Robot my) {
		return getSamplesListOf(my).size()>=Constants.MAX_SAMPLES;
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
	private List<Sample> getSamplesListIn(final int indexOf) {
		if(!samplesByOwner.containsKey(indexOf)) {
			final List<Sample> returned = new ArrayList<>();
			for(final Sample s : samples) {
				if(s.owner==indexOf) {
					returned.add(s);
				}
			}
			samplesByOwner.put(indexOf, Collections.unmodifiableList(returned));
		}
		return samplesByOwner.get(indexOf);
	}
	public List<Sample> getSamplesListOf(final MoleculeStore my) {
		/**
		 * maniac magic : as playfield is not in robots list, it returns -1, which is index for
		 * samples in cloud !
		 */
		return getSamplesListIn(robots.indexOf(my));

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

	public boolean canProvideMoleculesFor(final Robot robot, final Sample s) {
		final Map<Molecule, Integer> missingFromRobot = robot.findMissingFor(s);
		final Map<Molecule, Integer> missingFromStore = findMissingFor(missingFromRobot);
		final Map<Molecule, Integer> missing = missingFromStore;
		final boolean returned = canProvideMoleculesFor(missing);
		if(returned) {
			// finally, make sure we can put required molecules in robot
			int totalMoleculesLoaded = 0;
			for(final Molecule m : Molecule.values()) {
				totalMoleculesLoaded += robot.getAvailable().get(m);
				final int missingMolecule = missingFromRobot.get(m);
				if(missingMolecule>0) 
					totalMoleculesLoaded+=missingMolecule;
			}
			if(totalMoleculesLoaded>Constants.MAX_MOLECULES)
				return false;
		}
		return returned;
	}

	public static boolean canProvideMoleculesFor(final Map<Molecule, Integer> missing) {
		for(final Integer value : missing.values()) {
			if(value>0) {
				return false;
			}
		}
		return true;
	}
	public Playfield derive(final int distanceTo) {
		while(distanceTo>0) {
			final PlayfieldDeriver returned = (PlayfieldDeriver) new PlayfieldDeriver()
					.withRobots(robots)
					.withProjects(projects)
					.withSamples(samples);
			returned.addAllAvailable(getAvailable());
			returned.derive();
			return returned.derive(distanceTo-1);
		}
		return this;
	}

	public boolean canService(final Robot my) {
		final Playfield derived = derive(my.target.distanceTo(Module.MOLECULES));
		return !derived.samplesWithMolecules(my).isEmpty();
	}

	public List<Sample> findUnservicable(final Robot my) {
		final Playfield derived = derive(my.target.distanceTo(Module.MOLECULES));
		return derived.samplesWithoutMolecules(my);
	}
}
