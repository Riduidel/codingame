package org.ndx.codingame.hypersonic.entities;

import java.util.ArrayList;
import java.util.Collection;

import org.ndx.codingame.hypersonic.playground.PlaygroundAdapter;

public class GamerFinder extends PlaygroundAdapter<Collection<Gamer>> {
	public GamerFinder() {
		super(new ArrayList<>());
	}
	@Override
	public void visit(int x, int y, Content content) {
		if (content instanceof Gamer) {
			Gamer gamer = (Gamer) content;
			returned.add(gamer);
		}
	}
}