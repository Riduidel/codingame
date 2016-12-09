package org.ndx.codingame.hypersonic.entities;

import java.util.ArrayList;
import java.util.Collection;

import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;

public class GamerFinder extends PlaygroundAdapter<Collection<Gamer>, Content> {
	public GamerFinder() {
		super(new ArrayList<>());
	}
	@Override
	public void visit(final int x, final int y, final Content content) {
		if (content instanceof Gamer) {
			final Gamer gamer = (Gamer) content;
			returned.add(gamer);
		}
	}
}