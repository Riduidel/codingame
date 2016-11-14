package org.ndx.codingame.hypersonic;

import org.ndx.codingame.hypersonic.content.Content;

public class PlaygroundAdapter<Type> implements PlaygroundVisitor<Type>{
	public Type returned;
	public PlaygroundAdapter() { }
	public PlaygroundAdapter(Type defaultvalue) { returned = defaultvalue; }
	@Override public void startVisit(Playfield playground) { }
	@Override public void startVisitRow(int y) { }
	@Override public void endVisitRow(int y) { }
	@Override public Type endVisit(Playfield playground) { return returned; }
	@Override public void visit(int x, int y, Content content) {}
}