package org.ndx.codingame.code4life.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Robot {

	public final Module target;
	public final int eta;
	public final int score;
	public final Map<Molecule, Integer> counts;
	public final Map<Molecule, Integer> expertise;

	public Robot(final String target, final int eta, final int score, final Map<Molecule, Integer> counts,
			final Map<Molecule, Integer> expertise) {
		this.target = Module.valueOf(target);
		this.eta = eta;
		this.score = score;
		this.counts = counts;
		this.expertise = expertise;
	}

	public List<Molecule> findRequiredMoleculesFor(final Sample toProcess) {
		final List<Molecule> returned = new ArrayList<>();
		for(final Molecule type : Molecule.values()) {
			if(counts.get(type)<toProcess.cost.get(type)) {
				returned.add(type);
			}
		}
		return returned;
	}
}
