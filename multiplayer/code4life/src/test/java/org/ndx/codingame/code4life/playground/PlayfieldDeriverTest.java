package org.ndx.codingame.code4life.playground;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.code4life.entities.MoleculeStore.toMap;

import java.util.Arrays;

import org.junit.Test;
import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.Molecule;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;

public class PlayfieldDeriverTest  {

	@Test
	public void instances_are_different() {
		final Playfield tested = new Playfield(1);
		tested.withRobots(Arrays.asList(
				// First robot never impacts environment
				new Robot(Module.LABORATORY.name(), 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0	),

				new Robot(Module.LABORATORY.name(), 1, 2, 1, 8, 1, 0, 0, 0, 0, 0, 0, 0	)
				))
		.withSamples(Arrays.asList(
				new Sample(1, 1, 1, Molecule.A, 10, toMap(1, 0, 0, 0, 0))
				));
		final Playfield derived = tested.derive(1);
		assertThat(derived.robots).isNotSameAs(tested.robots);
		assertThat(derived.samples).isNotSameAs(tested.samples);
		assertThat(derived.getAvailable()).isNotSameAs(tested.getAvailable());
	}
	@Test
	public void can_let_robot_get_sample() {
		final Playfield tested = new Playfield(1);
		tested.withRobots(Arrays.asList(
				// First robot never impacts environment
				new Robot(Module.SAMPLES.name(), 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0	),

				new Robot(Module.SAMPLES.name(), 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0	)
				));
		Playfield derived = tested.derive(1);
		assertThat(derived.robots.get(0).eta).isEqualTo(0);
		assertThat(derived.samples).isEmpty();
		derived = derived.derive(1);
		assertThat(derived.robots.get(0).eta).isEqualTo(0);
		assertThat(derived.samples).isNotEmpty();
	}

	@Test
	public void can_let_robot_analyze_sample() {
		final Playfield tested = new Playfield(1);
		tested.withRobots(Arrays.asList(
				// First robot never impacts environment
				new Robot(Module.DIAGNOSIS.name(), 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0	),

				new Robot(Module.DIAGNOSIS.name(), 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0	)
				))
		.withSamples(Arrays.asList(
				new Sample(1, -1, 1, Molecule.A, 10, toMap(1, 0, 0, 0, 0)),
				new Sample(2, -1, 1, Molecule.A, 10, toMap(0, 1, 0, 0, 0)),
				new Sample(3, -1, 1, Molecule.A, 10, toMap(0, 0, 1, 0, 0)),
				new Sample(4, -1, 1, Molecule.A, 10, toMap(0, 0, 0, 1, 0)),
				new Sample(5, -1, 1, Molecule.A, 10, toMap(0, 0, 0, 0, 1)),
				new Sample(6, 1, 1, Molecule.A, 10, toMap(-1, -1, -1, -1, -1))
				))
		.addAllAvailable(toMap(1, 2, 3, 4, 5));
		Playfield derived = tested.derive(1);
		assertThat(derived.robots.get(0).eta).isEqualTo(0);
		derived = derived.derive(1);
		assertThat(derived.robots.get(0).eta).isEqualTo(0);
		assertThat(derived.samples.get(5).owner).isEqualTo(-1);
		derived = derived.derive(1);
		assertThat(derived.robots.get(0).target).isEqualTo(Module.DIAGNOSIS);
		assertThat(derived.robots.get(0).eta).isEqualTo(0);
		assertThat(derived.samples.get(0).owner).isEqualTo(1);
		derived = derived.derive(3);
		assertThat(derived.robots.get(1).target).isEqualTo(Module.MOLECULES);
		assertThat(derived.robots.get(0).target).isEqualTo(Module.DIAGNOSIS);
		assertThat(derived.samples.get(0).owner).isEqualTo(1);
		assertThat(derived.samples.get(1).owner).isEqualTo(1);
		assertThat(derived.samples.get(2).owner).isEqualTo(1);
	}

	@Test
	public void can_let_robot_get_molecules() {
		final Playfield tested = new Playfield(1);
		tested.withRobots(Arrays.asList(
				// First robot never impacts environment
				new Robot(Module.MOLECULES.name(), 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0	),

				new Robot(Module.MOLECULES.name(), 1, 2, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0	)
				))
		.withSamples(Arrays.asList(
				new Sample(1, 1, 1, Molecule.A, 10, toMap(1, 0, 0, 0, 0)),
				new Sample(2, 1, 1, Molecule.A, 10, toMap(0, 1, 0, 0, 0)),
				new Sample(3, 1, 1, Molecule.A, 10, toMap(0, 0, 1, 0, 0)),
				new Sample(4, -1, 1, Molecule.A, 10, toMap(0, 0, 0, 1, 0)),
				new Sample(5, -1, 1, Molecule.A, 10, toMap(0, 0, 0, 0, 1)),
				new Sample(6, -1, 1, Molecule.A, 10, toMap(-1, -1, -1, -1, -1))
				))
		.addAllAvailable(toMap(1, 2, 3, 4, 5));
		Playfield derived = tested.derive(1);
		assertThat(derived.robots.get(0).eta).isEqualTo(0);
		derived = derived.derive(1);
		assertThat(derived.robots.get(0).eta).isEqualTo(0);
		assertThat(derived.robots.get(1).getAvailable().get(Molecule.A)).isEqualTo(1);
		derived = derived.derive(1);
		assertThat(derived.robots.get(1).getAvailable().get(Molecule.A)).isEqualTo(1);
		assertThat(derived.robots.get(1).getAvailable().get(Molecule.B)).isEqualTo(9);
		derived = derived.derive(1);
		assertThat(derived.robots.get(1).target).isEqualTo(Module.LABORATORY);
		assertThat(derived.robots.get(0).target).isEqualTo(Module.MOLECULES);
	}

	@Test
	public void can_let_robot_drop_to_lab() {
		final Playfield tested = new Playfield(1);
		tested.withRobots(Arrays.asList(
				// First robot never impacts environment
				new Robot(Module.LABORATORY.name(), 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0	),

				new Robot(Module.LABORATORY.name(), 1, 2, 1, 8, 1, 0, 0, 0, 0, 0, 0, 0	)
				))
		.withSamples(Arrays.asList(
				new Sample(1, 1, 1, Molecule.A, 10, toMap(1, 0, 0, 0, 0)),
				new Sample(2, 1, 1, Molecule.A, 5, toMap(0, 1, 0, 0, 0)),
				new Sample(3, 1, 1, Molecule.A, 1, toMap(0, 0, 1, 0, 0)),
				new Sample(4, -1, 1, Molecule.A, 10, toMap(0, 0, 0, 1, 0)),
				new Sample(5, -1, 1, Molecule.A, 10, toMap(0, 0, 0, 0, 1)),
				new Sample(6, -1, 1, Molecule.A, 10, toMap(-1, -1, -1, -1, -1))
				))
		.addAllAvailable(toMap(1, 2, 3, 4, 5));
		Playfield derived = tested.derive(1);
		assertThat(derived.robots.get(0).eta).isEqualTo(0);
		derived = derived.derive(1);
		assertThat(derived.robots.get(0).eta).isEqualTo(0);
		assertThat(derived.robots.get(1).score).isEqualTo(12);
		assertThat(derived.samples.size()).isEqualTo(5);
		assertThat(derived.samples.get(0).id).isEqualTo(2);
		derived = derived.derive(1);
		assertThat(derived.robots.get(1).score).isEqualTo(17);
		assertThat(derived.samples.size()).isEqualTo(4);
		assertThat(derived.samples.get(0).id).isEqualTo(3);
		derived = derived.derive(1);
		assertThat(derived.robots.get(1).score).isEqualTo(18);
		assertThat(derived.samples.size()).isEqualTo(3);
		assertThat(derived.samples.get(0).id).isEqualTo(4);
	}
}
