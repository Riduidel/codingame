package org.ndx.codingame.hypersonic.content;

import org.ndx.codingame.hypersonic.CanFire;

public interface Content {
	public <Type> Type accept(ContentVisitor<Type> visitor);

	public CanFire canFire();

	public boolean canBeWalkedOn();
}