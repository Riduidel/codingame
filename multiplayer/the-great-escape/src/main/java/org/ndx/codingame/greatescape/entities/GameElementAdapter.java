package org.ndx.codingame.greatescape.entities;

public abstract class GameElementAdapter<Returned> implements GameElementVisitor<Returned> {
	private Returned defaultValue;
	
	public GameElementAdapter() {
		this(null);
	}
	public GameElementAdapter(final Returned value) {
		defaultValue = value;
	}

	@Override
	public Returned visitGamer(final Gamer gamer) {
		return defaultVisit(gamer);
	}

	@Override
	public Returned visitNothing(final Nothing nothing) {
		return defaultVisit(nothing);
	}

	@Override
	public Returned visitWall(final Wall wall) {
		return defaultVisit(wall);
	}
	
	protected Returned defaultVisit(final GameElement nothing) {
		return defaultValue;
	}

}
