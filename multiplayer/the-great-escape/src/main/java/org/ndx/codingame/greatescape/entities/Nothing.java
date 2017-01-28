package org.ndx.codingame.greatescape.entities;

public class Nothing implements GameElement {
	
	public static final GameElement instance = new Nothing();
	@Override
	public <Returned> Returned accept(final GameElementVisitor<Returned> visitor) {
		return visitor.visitNothing(this);
	}
	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		return new StringBuilder();
	}

}
