package org.ndx.codingame.code4life;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.ndx.codingame.code4life.actions.ConnectToDiagnostic;
import org.ndx.codingame.code4life.actions.ConnectToDistribution;
import org.ndx.codingame.code4life.actions.ConnectToLaboratory;
import org.ndx.codingame.code4life.actions.Goto;
import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.MoleculeType;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.gaming.actions.Action;

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory
 * with enough molecules to produce medicine!
 **/
public class Player {

	public static void main(final String args[]) {
		final Scanner in = new Scanner(System.in);
		final int projectCount = in.nextInt();
		for (int i = 0; i < projectCount; i++) {
			final int a = in.nextInt();
			final int b = in.nextInt();
			final int c = in.nextInt();
			final int d = in.nextInt();
			final int e = in.nextInt();
		}

		// game loop
		while (true) {
			Robot my = null;
			for (int i = 0; i < 2; i++) {
				// module where player is
				final String target = in.next();
				final int eta = in.nextInt();
				// health points of player
				final int score = in.nextInt();
				final Map<MoleculeType, Integer> counts = new EnumMap<>(MoleculeType.class);
				// number of molecules of each type
				for (final MoleculeType type : MoleculeType.values()) {
					counts.put(type, in.nextInt());
				}
				final Map<MoleculeType, Integer> expertise = new EnumMap<>(MoleculeType.class);
				// number of molecules of each type
				for (final MoleculeType type : MoleculeType.values()) {
					expertise.put(type, in.nextInt());
				}
				if (my == null) {
					my = new Robot(target, eta, score, counts, expertise);
				}
			}
			final Map<MoleculeType, Integer> available = new EnumMap<>(MoleculeType.class);
			// number of molecules of each type
			for (final MoleculeType type : MoleculeType.values()) {
				available.put(type, in.nextInt());
			}
			Collection<Sample> samples = new ArrayList<>();
			final int sampleCount = in.nextInt();
			for (int i = 0; i < sampleCount; i++) {
				final int sampleId = in.nextInt();
				final int carriedBy = in.nextInt();
				final int rank = in.nextInt();
				final String expertiseGain = in.next();
				final int health = in.nextInt();
				final Map<MoleculeType, Integer> cost = new EnumMap<>(MoleculeType.class);
				// number of molecules of each type
				for (final MoleculeType type : MoleculeType.values()) {
					cost.put(type, in.nextInt());
				}
				samples.add(new Sample(sampleId, carriedBy, rank, expertiseGain, health, cost));
			}

			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");
			samples = samples.stream()
					.sorted(Comparator.comparingInt(Sample::getHealth).reversed())
					.collect(Collectors.toList());
			System.out.println(computeMoves(my, samples));
		}
	}

	private static String computeMoves(final Robot my, final Collection<Sample> samples) {
		return computeMovesOf(my, samples).toCommandString();
	}

	private static Action computeMovesOf(final Robot my, final Collection<Sample> samples) {
		final List<Sample> onRobot = samples.stream().filter((s) -> s.owner == 0).collect(Collectors.toList());
		if(onRobot.isEmpty()) {
			if(my.target.equals(Module.DIAGNOSIS)) {
				final List<Sample> inCloud = samples.stream().filter((s) -> s.owner < 0).collect(Collectors.toList());
				// Get sample giving best score
				final Sample toCollect = inCloud.stream()
						.findFirst()
						.get();
				return new ConnectToDiagnostic(toCollect);
			} else {
				// Robot has no sample. Go to Diagnosis
				return new Goto(Module.DIAGNOSIS);
			}
		} else {
			// Just take first sample as reference
			final Sample toProcess = onRobot.get(0);
			if(my.target.equals(Module.MOLECULES)) {
				final List<MoleculeType> required = my.findRequiredMoleculesFor(toProcess);
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
		}
	}
}