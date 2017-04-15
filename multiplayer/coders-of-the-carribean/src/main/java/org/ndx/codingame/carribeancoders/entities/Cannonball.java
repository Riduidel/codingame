package org.ndx.codingame.carribeancoders.entities;

public class Cannonball extends Entity {

	private final int gunner;
	private final int duration;

	public Cannonball(final int x, final int y, final int arg1, final int arg2) {
		super(x, y);
		gunner = arg1;
		duration = arg2;
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = super.toUnitTestConstructorPrefix(multilinePrefix);
		returned
			.append(gunner).append(", ")
			.append(duration).append(")")
			;
		return returned;
	}

	@Override
	public <Type> Type accept(final EntityVisitor<Type> visitor) {
		return visitor.visitCannonball(this);
	}

}
