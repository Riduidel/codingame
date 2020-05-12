package org.ndx.codingame.spring.challenge.entities;

public class Wall implements Content {
	public static final char CHARACTER = '#';
	public static Wall instance = new Wall();

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitWall(this);
	}

	@Override
	public boolean canBeWalkedOn() {
		return false;
	}

	@Override
	public int score() {
		return Integer.MIN_VALUE;
	}
}