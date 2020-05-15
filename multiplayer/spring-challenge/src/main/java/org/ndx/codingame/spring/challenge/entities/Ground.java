package org.ndx.codingame.spring.challenge.entities;

public class Ground implements Content {
	public static final char CHARACTER = ' ';
	public static Ground instance = new Ground();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitGround(this);
	}

	@Override
	public boolean canBeWalkedOnBy(AbstractPac pac) {
		return true;
	}

	@Override
	public int score() {
		return 0;
	}
	
	@Override
	public String toString() {
		return Character.toString(CHARACTER);
	}
}