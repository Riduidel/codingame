package org.ndx.codingame.hypersonic;

import java.util.ArrayList;
import java.util.Collection;

import org.ndx.codingame.hypersonic.content.Bomb;
import org.ndx.codingame.hypersonic.content.Content;
import org.ndx.codingame.hypersonic.content.ContentAdapter;
import org.ndx.codingame.hypersonic.content.ContentVisitor;

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