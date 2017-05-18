package org.ndx.codingame.code4life.playground.states;

import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ndx.codingame.code4life.actions.ConnectToDistribution;
import org.ndx.codingame.code4life.actions.Goto;
import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.Molecule;
import org.ndx.codingame.code4life.entities.MoleculeStore;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public class Servicable extends StatedComputer {
	public Servicable(final Playfield playfield) {
		super(playfield);
	}

	@Override
	public Action compute(final Robot my) {
		final Deque<Molecule> byRarity = playfield.moleculesByRarity();

		final SortedSet<Sample> toService = new TreeSet<>(Sample.BY_DESCENDING_HEALTH);
		toService.addAll(playfield.getSamplesListOf(my));
		Map<Molecule, Integer> usable = new EnumMap<>(my.getAvailable());
		for(final Sample s : toService) {
			usable = MoleculeStore.substract(MoleculeStore.add(usable, my.expertise), s.cost);
			boolean hasOneFilledSample = false;
			for(final Molecule m : byRarity) {
				if(usable.get(m)<0) {
					if(my.isFullOfMolecules() || playfield.getAvailable().get(m)<=0) {
						if(hasOneFilledSample) {
							return new Goto(Module.DIAGNOSIS);
						} else {
							return new Goto(Module.LABORATORY);
						}
					} else {
						return new ConnectToDistribution(m);
					}
				}
			}
			hasOneFilledSample = true;
		}
		// If we have molecules for all samples and still have some room, take some time
		// to take adversary down
		if(!my.isFullOfMolecules()) {
			for(final Molecule m : byRarity) {
				if(playfield.getAvailable().get(m)>0) {
					return new ConnectToDistribution(m);
				}
			}
		}
		return new Goto(Module.LABORATORY);
	}

}
