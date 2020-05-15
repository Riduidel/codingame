package org.ndx.codingame.spring.challenge.entities;

public interface Content {
	public <Type> Type accept(ContentVisitor<Type> visitor);

	/**
	 * 
	 * @param pac BEWARE pac can (and is sometimes - typically in cache init) null
	 * @return
	 */
	boolean canBeWalkedOnBy(AbstractPac pac);

	public int score();

	/**
	 * Invoked on advance turn when this cell is in viewsight of one of our alive pacs
	 * @return
	 */
	public default boolean revealsGround() {
		return false;
	}
}
