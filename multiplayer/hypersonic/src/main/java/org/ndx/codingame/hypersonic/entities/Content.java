package org.ndx.codingame.hypersonic.entities;

public interface Content {
	public <Type> Type accept(ContentVisitor<Type> visitor);

	public CanFire canFire();

	public boolean canBeWalkedOn();
}