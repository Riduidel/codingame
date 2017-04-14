package org.ndx.codingame.carribeancoders.entities;

public class Mine extends Entity {

	public Mine(final int x, final int y) {
		super(x, y);
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = super.toUnitTestConstructorPrefix(multilinePrefix);
		returned.replace(returned.length()-2, returned.length(), "");
		returned
			.append(")")
			;
		return returned;
	}

}
