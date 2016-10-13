package org.ndx.codingame.hypersonic;

public class PlaygroundAdapter<Type> implements PlaygroundVisitor<Type>{
	public Type returned;
	public PlaygroundAdapter() { }
	public PlaygroundAdapter(Type defaultvalue) { returned = defaultvalue; }
	@Override public void startVisit(Playground playground) { }
	@Override public void startVisitRow(int y) { }
	@Override public void endVisitRow(int y) { }
	@Override public Type endVisit(Playground playground) { return returned; }
	@Override public void visit(int x, int y, Content content) {}
}