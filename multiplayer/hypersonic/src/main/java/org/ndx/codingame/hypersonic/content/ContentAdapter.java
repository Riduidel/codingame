package org.ndx.codingame.hypersonic.content;

import org.ndx.codingame.hypersonic.Gamer;

public class ContentAdapter<Type> implements ContentVisitor<Type> {
	private Type returned = null;

	public ContentAdapter() { }
	public ContentAdapter(Type returned) { this.returned = returned; }
	@Override public Type visitNothing(Nothing nothing) { return returned; }
	@Override public Type visitBox(Box box) { return returned; }
	@Override public Type visitWall(Wall wall) { return returned; }
	@Override public Type visitGamer(Gamer bomber) { return returned; }
	@Override public Type visitBomb(Bomb bomb) { return returned; }
	@Override public Type visitItem(Item item) { return returned; }
	@Override public Type visitFire(Fire fire) { return returned; }
	@Override public Type visitFireThenItem(FireThenItem fire) { return returned; }
}