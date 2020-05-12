package org.ndx.codingame.spring.challenge.entities;

public interface Content {
	public <Type> Type accept(ContentVisitor<Type> visitor);

	boolean canBeWalkedOn();

	public int score();

	public default Content advanceOneTurn() {
		return Ground.instance;
	}
}
