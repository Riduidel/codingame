package org.ndx.codingame.code4life.playground.states;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.ndx.codingame.code4life.Constants;
import org.ndx.codingame.code4life.actions.ConnectToDiagnostic;
import org.ndx.codingame.code4life.actions.Goto;
import org.ndx.codingame.code4life.entities.Module;
import org.ndx.codingame.code4life.entities.Robot;
import org.ndx.codingame.code4life.entities.Sample;
import org.ndx.codingame.code4life.playground.Playfield;
import org.ndx.codingame.gaming.actions.Action;

public class Diagnosed extends StatedComputer {

	public Diagnosed(final Playfield playfield) {
		super(playfield);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action compute(final Robot my) {
		final List<Sample> mySamples = new ArrayList<>(playfield.getSamplesListOf(my));
		final SortedSet<Sample> myInterestingSamples = findInterestingSamplesIn(playfield, my, mySamples);
		if(myInterestingSamples.size()<mySamples.size()) {
			for(final Sample s : myInterestingSamples) {
				// removeAll behaves really strangely
				mySamples.remove(s);
			}
			return new ConnectToDiagnostic(mySamples.get(0));
		} else {
			final SortedSet<Sample> interestingInCloud = findInterestingSamplesIn(playfield, my, playfield.getSamplesListOf(playfield));
			if(interestingInCloud.isEmpty()) {
				if(mySamples.isEmpty()) {
					return new Goto(Module.SAMPLES);
				} else {
					return new Goto(Module.MOLECULES);
				}
			} else {
				if(mySamples.size()>=Constants.MAX_SAMPLES) {
					return new Goto(Module.MOLECULES);
				} else {
					return new ConnectToDiagnostic(interestingInCloud.first());
				}
			}
		}
	}

}
