package org.ndx.codingame.hypersonic;

public interface Content {
	public <Type> Type accept(ContentVisitor<Type> visitor);

	public CanFire canFire();

	public boolean canBeWalkedOn();
}