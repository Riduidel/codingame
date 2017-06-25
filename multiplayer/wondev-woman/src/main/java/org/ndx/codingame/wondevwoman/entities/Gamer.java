package org.ndx.codingame.wondevwoman.entities;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class Gamer implements ConstructableInUnitTest {
	private static final String CLASS_NAME = Gamer.class.getSimpleName();
	public final DiscretePoint position;
	public final int index;

	public Gamer(final DiscretePoint position, final int i) {
		super();
		this.position = position;
		index = i;
	}

	public Gamer(final int x, final int y, final int i) {
		this(new DiscretePoint(x, y), i);
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		return new StringBuilder("g(")
				.append(position.x).append(", ")
				.append(position.y).append(", ")
				.append(index).append(")")
				;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Gamer [position=");
		builder.append(position);
		builder.append(", index=");
		builder.append(index);
		builder.append("]");
		return builder.toString();
	}
}
