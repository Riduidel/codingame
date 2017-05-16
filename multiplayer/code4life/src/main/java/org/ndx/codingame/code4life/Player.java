package org.ndx.codingame.code4life;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.ndx.codingame.code4life.entities.Molecule;
import org.ndx.codingame.code4life.entities.MoleculeStore;
import org.ndx.codingame.code4life.entities.Project;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.tounittest.ToUnitTestStringBuilder;

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory
 * with enough molecules to produce medicine!
 **/
public class Player {

	public static void main(final String args[]) {
		final Scanner in = new Scanner(System.in);
		final int projectCount = in.nextInt();
		final List<Project> projects = new ArrayList<>();
		for (int i = 0; i < projectCount; i++) {
			final int a = in.nextInt();
			final int b = in.nextInt();
			final int c = in.nextInt();
			final int d = in.nextInt();
			final int e = in.nextInt();
			projects.add(new Project(MoleculeStore.toMap(a, b, c, d, e)));
		}

		// game loop
		while (true) {
			final Playfield playfield = new Playfield();
			playfield.withProjects(projects);
			final MoleculeStore my = null;
			final List<Robot> robots= new ArrayList<>();
			for (int i = 0; i < 2; i++) {
				// module where player is
				final String target = in.next();
				final int eta = in.nextInt();
				// health points of player
				final int score = in.nextInt();
				final Map<Molecule, Integer> counts =
						MoleculeStore.toMap(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
				final Map<Molecule, Integer> expertise =
						MoleculeStore.toMap(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
				robots.add(new Robot(target, eta, score, counts, expertise));
			}
			playfield.withRobots(robots);
			playfield.addAllAvailable(MoleculeStore.toMap(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()));
			final Collection<Sample> samples = new ArrayList<>();
			final int sampleCount = in.nextInt();
			for (int i = 0; i < sampleCount; i++) {
				final int sampleId = in.nextInt();
				final int carriedBy = in.nextInt();
				final int rank = in.nextInt();
				final String expertiseGain = in.next();
				final int health = in.nextInt();
				final Map<Molecule, Integer> cost = new EnumMap<>(Molecule.class);
				// number of molecules of each type
				for (final Molecule type : Molecule.values()) {
					cost.put(type, in.nextInt());
				}
				samples.add(new Sample(sampleId, carriedBy, rank, expertiseGain, health, cost));
			}
			playfield.withSamples(samples);

			System.err.println(ToUnitTestStringBuilder.canComputeAt(playfield));
			System.out.println(playfield.computeMoves());
		}
	}
}