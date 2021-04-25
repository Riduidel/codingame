package org.ndx.codingame.spring.challenge.entities;

public class ContentAdapter<Type> implements ContentVisitor<Type> {
	protected Type returnedFromContent = null;

	public ContentAdapter() { }
	public ContentAdapter(final Type returned) { this.returnedFromContent = returned; }
	@Override public Type visitNothing(Nothing nothing) { return returnedFromContent; }
	@Override
	public Type visitGround(Ground ground) {
		return returnedFromContent;
	}
	@Override
	public Type visitWall(Wall wall) {
		return returnedFromContent;
	}
	@Override
	public Type visitBigPill(BigPill bigPill) {
		return returnedFromContent;
	}
	@Override
	public Type visitSmallPill(SmallPill smallPill) {
		return returnedFromContent;
	}
	@Override
	public Type visitPac(Pac pac) {
		return returnedFromContent;
	}
	@Override
	public Type visitPotentialSmallPill(PotentialSmallPill potentialSmallPill) {
		return returnedFromContent;
	}
	@Override
	public Type visitPacTrace(PacTrace pacTrace) {
		return returnedFromContent;
	}
	@Override
	public Type visitVirtualPac(VirtualPac virtualPac) {
		return returnedFromContent;
	}
}