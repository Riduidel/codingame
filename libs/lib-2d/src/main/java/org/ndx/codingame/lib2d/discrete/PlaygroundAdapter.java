package org.ndx.codingame.lib2d.discrete;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.PlaygroundVisitor;

public class PlaygroundAdapter<Returned, Content> implements PlaygroundVisitor<Returned, Content>{
	public Returned returned;
	public PlaygroundAdapter() { }
	public PlaygroundAdapter(final Returned defaultvalue) { returned = defaultvalue; }
	@Override public void startVisit(final ImmutablePlayground<Content> playground) { }
	@Override public void startVisitRow(final int y) { }
	@Override public void endVisitRow(final int y) { }
	@Override public Returned endVisit(final ImmutablePlayground<Content> playground) { return returned; }
	@Override public void visit(final int x, final int y, final Content content) {}
}