package org.ndx.codingame.spring.challenge.entities;

public class Nothing implements Content {
	public static Nothing instance = new Nothing();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitNothing(this);
	}

	@Override
	public boolean canBeWalkedOnBy(AbstractPac pac) {
		return false;
	}

	@Override
	public int score() {
		return 0;
	}
	
	@Override
	public String toString() {
		return Character.toString('X');
	}
}