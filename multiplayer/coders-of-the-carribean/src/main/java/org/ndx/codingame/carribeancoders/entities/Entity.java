package org.ndx.codingame.carribeancoders.entities;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public abstract class Entity implements ConstructableInUnitTest  {
	public final DiscretePoint position;

	protected Entity(final DiscretePoint position) {
		this.position = position;
	}

	protected Entity(final int x, final int y) {
		this(new DiscretePoint(x, y));
	}

	public StringBuilder toUnitTestConstructorPrefix(final String multilinePrefix) {
		return new StringBuilder("new ").append(getClass().getSimpleName()).append("(")
				.append(position.x).append(", ")
				.append(position.y).append(", ")
				;
	}
	
	public abstract <Type> Type accept(EntityVisitor<Type> visitor);
}