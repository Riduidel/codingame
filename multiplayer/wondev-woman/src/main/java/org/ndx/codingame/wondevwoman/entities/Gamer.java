package org.ndx.codingame.wondevwoman.entities;

import java.util.Collection;
import java.util.List;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.wondevwoman.actions.LegalAction;
import org.ndx.codingame.wondevwoman.playground.Playfield;

public class Gamer {
	private final DiscretePoint position;
	private final int index;

	public Gamer(final DiscretePoint position, final int i) {
		super();
		this.position = position;
		index = i;
	}

	public Gamer(final int x, final int y, final int i) {
		this(new DiscretePoint(x, y), i);
	}

	public String compute(final Playfield playfield, final List<Gamer> enemy, final Collection<LegalAction> actions) {
		return actions.stream()
				.filter((a) -> a.playerIndex==index)
				.findFirst()
				.get()
				.toCommandString();
	}
}
