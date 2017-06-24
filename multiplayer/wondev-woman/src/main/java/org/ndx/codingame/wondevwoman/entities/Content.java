package org.ndx.codingame.wondevwoman.entities;

public interface Content {
	public <Returned> Returned accept(ContentVisitor<Returned> visitor);
}
