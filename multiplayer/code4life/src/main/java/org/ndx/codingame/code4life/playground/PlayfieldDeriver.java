package org.ndx.codingame.code4life.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.Molecule;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;

public class PlayfieldDeriver extends Playfield {

	void derive() {
		final List<Robot> mutators = new ArrayList<>(robots);
		robots.clear();
		for (final Robot r : mutators) {
			derive(r, mutators.indexOf(r) != 0);
		}
	}

	private Robot derive(final Robot r, final boolean alterEnvironment) {
		final Robot derived = new Robot(r.target.name(), Math.max(r.eta - 1, 0), r.score, r.getAvailable(),
				r.expertise);
		robots.add(derived);
		if (alterEnvironment && derived.eta == 0 && r.eta == 0) {
			// Alter environment
			switch (derived.target) {
			case START_POS:
				break;
			case SAMPLES:
				return deriveOnSamples(derived);
			case DIAGNOSIS:
				return deriveOnDiagnosis(derived);
			case MOLECULES:
				return deriveOnMolecules(derived);
			case LABORATORY:
				return deriveOnLaboratory(derived);
			}
		}
		return derived;
	}

	private Robot deriveOnLaboratory(final Robot derived) {
		final List<Sample> samplesList = getSamplesListOf(derived);
		if(samplesList.isEmpty()) {
			return redirect(derived, Module.SAMPLES);
		} else {
			final List<Sample> selector = new ArrayList<>(samplesList);
			selector.sort(Sample.BY_DESCENDING_HEALTH);
			final Sample toDrop = selector.get(0);
			return scoring(derived, toDrop);
		}
	}

	private Robot deriveOnMolecules(final Robot derived) {
		if(derived.isFullOfMolecules()) {
			return redirect(derived, Module.LABORATORY);
		} else {
			final Map<Molecule, Integer> usable = new EnumMap<>(derived.getAvailable());
			for(final Sample s : getSamplesListOf(derived)) {
				final Map<Molecule, Integer> missing = substract(s.cost, add(usable, derived.expertise));
				if(!missing.isEmpty()) {
					final Collection<Molecule> rarest = moleculesByRarity();
					for(final Molecule m : rarest) {
						if(missing.containsKey(m)) {
							final Integer available = getAvailable().get(m);
							if(available>0) {
								getAvailable().put(m, available-1);
								derived.getAvailable().put(m, derived.getAvailable().get(m)+1);
								return derived;
							}
						}
					}
				}
			}
			return redirect(derived, Module.DIAGNOSIS);
		}
	}

	private Robot deriveOnDiagnosis(final Robot derived) {
		if (isFullOfDiagnosedSamples(derived)) {
			return redirect(derived, Module.MOLECULES);
		} else {
			// Is there any unanalyzed sample ?
			final Optional<Sample> toStoreInCloud = getFirstUndiagnosedSample(derived);
			if (toStoreInCloud.isPresent()) {
				moveTo(toStoreInCloud.get(), -1);
				return derived;
			}
			/* Get the samples with the less number of molecules available */
			final Collection<Molecule> rarest = moleculesByRarity();
			final List<Sample> inCloud = getSamplesListOf(this);
			for (final Molecule m : rarest) {
				for (final Sample s : inCloud) {
					if (s.cost.get(m) > 0 && s.owner<0) {
						moveTo(s, 1);
						return derived;
					}
				}
			}
			return derived;
		}
	}

	/**
	 * Do not forget to put molecules back in game !
	 * @param derived
	 * @param toDrop
	 * @return
	 */
	private Robot scoring(final Robot derived, final Sample toDrop) {
		final Robot scored = derived.scoreTo(toDrop);
		robots.set(robots.indexOf(derived), scored);
		samples.remove(toDrop);
		for(final Molecule m : Molecule.values()) {
			getAvailable().put(m, getAvailable().get(m)+toDrop.cost.get(m));
		}
		return scored;
	}

	private Robot redirect(final Robot derived, final Module diagnosis) {
		final Robot redirected = derived.redirectTo(diagnosis);
		robots.set(robots.indexOf(derived), redirected);
		return redirected;
	}

	private void moveTo(final Sample s, final int robotIndex) {
		final int index = samples.indexOf(s);
		samples.set(index, s.redirectTo(robotIndex));
	}

	private Robot deriveOnSamples(final Robot derived) {
		if (isFullOfSamples(derived)) {
			return redirect(derived, Module.DIAGNOSIS);
		} else {
			samples.add(new Sample(samples.size(), 1, 3, Molecule.A.name(), 10, -1, -1, -1, -1, -1));
			return derived;
		}
	}

}
