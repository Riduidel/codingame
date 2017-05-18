package org.ndx.codingame.code4life.playground.states;

import org.ndx.codingame.code4life.Constants;
import org.ndx.codingame.code4life.actions.ConnectToSampler;
import org.ndx.codingame.code4life.entities.MoleculeStore;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public class NotEnough extends StatedComputer {

	public NotEnough(final Playfield playfield) {
		super(playfield);
	}

	/**
	 * Connect to samples and get one of the most suited type
	 */
	@Override
	public Action compute(final Robot my) {
		int totalExpertise = MoleculeStore.totalCostOf(my.expertise);
		for(final Sample s : playfield.getSamplesListOf(my)) {
			totalExpertise -= s.rank*Constants.RANK_FACTOR;
		}
		return new ConnectToSampler(Math.min(Math.max(totalExpertise, 1), 3));
	}

}
