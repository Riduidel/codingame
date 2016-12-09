package org.ndx.codingame.hypersonic.entities;

public class ContentAdapter<Type> implements ContentVisitor<Type> {
	protected Type returnedFromContent = null;

	public ContentAdapter() { }
	public ContentAdapter(final Type returned) { this.returnedFromContent = returned; }
	@Override public Type visitNothing(final Nothing nothing) { return returnedFromContent; }
	@Override public Type visitBox(final Box box) { return returnedFromContent; }
	@Override public Type visitWall(final Wall wall) { return returnedFromContent; }
	@Override public Type visitGamer(final Gamer bomber) { return returnedFromContent; }
	@Override public Type visitBomb(final Bomb bomb) { return returnedFromContent; }
	@Override public Type visitItem(final Item item) { return returnedFromContent; }
	@Override public Type visitFire(final Fire fire) { return returnedFromContent; }
	@Override public Type visitFireThenItem(final FireThenItem fire) { return returnedFromContent; }
}