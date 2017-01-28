package org.ndx.codingame.greatescape.entities;

public class Wall implements GameElement {
	public final Orientation direction;

	public Wall(final Orientation direction) {
		this.direction = direction;
	}

	@Override
	public <Returned> Returned accept(final GameElementVisitor<Returned> visitor) {
		return visitor.visitWall(this);
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder("new Wall(");
		returned.append("Orientation.");
		returned.append(direction);
		returned.append(")");
		return returned;
	}

}
