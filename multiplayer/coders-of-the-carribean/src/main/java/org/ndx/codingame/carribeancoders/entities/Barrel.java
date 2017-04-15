package org.ndx.codingame.carribeancoders.entities;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class Barrel extends Entity  {
	public final int rum;

	public Barrel(final int x, final int y, final int rum) {
		this(new DiscretePoint(x, y), rum);
	}
	public Barrel(final DiscretePoint position, final int rum) {
		super(position);
		this.rum = rum;
	}
	
	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = super.toUnitTestConstructorPrefix(multilinePrefix);
		returned
			.append(rum).append(")")
			;
		return returned;
	}
	@Override
	public <Type> Type accept(final EntityVisitor<Type> visitor) {
		return visitor.visitBarrel(this);
	}
}