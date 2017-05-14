package org.ndx.codingame.code4life.entities;

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
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public enum Module {
	START_POS {
		@Override
		public String loaded() {
			return name()+" loaded";
		}
		@Override
		public Action computeMoveOf(final Robot my, final Playfield playfield) {
			return new Goto(SAMPLES);
		}
	}, SAMPLES {
		@Override
		public String loaded() {
			return name()+" loaded";
		}
		@Override
		public Action computeMoveOf(final Robot my, final Playfield playfield, final List<Sample> mySamples) {
			if(mySamples.size()<Constants.MAX_SAMPLES) {
				// Get one sample of the most interesting type
				// TODO compute type dynamically
				return new ConnectToSampler(1);
			} else {
				return new Goto(DIAGNOSIS);
			}
		}
	}, DIAGNOSIS {
		@Override
		public String loaded() {
			return name()+" loaded";
		}
		@Override
		public Action computeMoveOf(final Robot my, final Playfield playfield, final List<Sample> mySamples) {
			// Do we have any unanalyzed sample ?
			// If so, publish it on cloud
			final List<Sample> unanalyzed = mySamples.stream()
				.filter((s) -> !s.isDiagnosed())
				.collect(Collectors.toList());
			if(unanalyzed.isEmpty()) {
				// No unanalyzed sample ? cool
				// wait ... do we have enough sample ?
				if(mySamples.size()<Constants.MAX_SAMPLES) {
					final Optional<Sample> bestSample = my.findBestSampleIn(playfield.getProcessableSamplesInCloud());
					if(bestSample.isPresent()) {
						return new ConnectToDiagnostic(bestSample.get());
					} else {
						if(mySamples.isEmpty()) {
							return new Goto(SAMPLES);
						} else {
							// We have not filled our collection, but can't fill it any more, so give up
							// and jump on molecules
							return new Goto(MOLECULES);
						}
					}
					// TODO can all owned samples be processed ? If not, release sample
				} else {
					// What are we waiting ? Jump to molecules ! (TODO unless we have enough molecules)
					return new Goto(MOLECULES);
				}
			} else {
				return new ConnectToDiagnostic(unanalyzed.get(0));
			}
		}
	}, MOLECULES {
		@Override
		public String loaded() {
			return name()+" loaded";
		}
		@Override
		public Action computeMoveOf(final Robot my, final Playfield playfield, final List<Sample> mySamples) {
			if(!my.isFull()) {
				final List<Sample> byHealth = mySamples.stream()
					.sorted(Comparator.comparingInt(Sample::getHealth))
					.collect(Collectors.toList());
				final EnumMap<Molecule, Integer> remaining = new EnumMap<>(my.counts);
				for(final Sample s : byHealth) {
					final Map<Molecule, Integer> missing = my.findMissingFor(s);
					if(!missing.isEmpty()) {
						final Molecule molecule = missing.keySet().iterator().next();
						if(playfield.getAvailable().get(molecule)>0) {
							return new ConnectToDistribution(molecule);
						}
					}
				}
			}
			return new Goto(LABORATORY);
		}
	}, LABORATORY {
		@Override
		public String loaded() {
			return name()+" loaded";
		}
		@Override
		public Action computeMoveOf(final Robot my, final Playfield playfield, final List<Sample> mySamples) {
			final List<Sample> byHealth = mySamples.stream()
					.sorted(Comparator.comparingInt(Sample::getHealth))
					.collect(Collectors.toList());
			if(byHealth.isEmpty()) {
				// Are there any samples in diagnosis ?
				if(playfield.getProcessableSamplesInCloud().isEmpty()) {
					return new Goto(Module.SAMPLES);
				} else {
					return new Goto(Module.DIAGNOSIS);
				}
			} else {
				// Do we have enough molecules to process first sample ?
				for(final Sample sample : byHealth) {
					if(my.canSendToLaboratory(sample)) {
						return new ConnectToLaboratory(sample);
					}
				}
				// TODO check if we can just reload molecules
				return new Goto(Module.DIAGNOSIS);
			}
		}
	};

	public Action computeMoveOf(final Robot my, final Playfield playfield) {
		final List<Sample> mySamples = playfield.getSamplesOf(my);
		return computeMoveOf(my, playfield, mySamples);
	}

	public Action computeMoveOf(final Robot my, final Playfield playfield, final List<Sample> mySamples) {
		throw new UnsupportedOperationException("Il faut implémenter "+name()+"#computeMoveOf(...)");
	}

	public String loaded() {
		return "not yet loaded";
	}
}
