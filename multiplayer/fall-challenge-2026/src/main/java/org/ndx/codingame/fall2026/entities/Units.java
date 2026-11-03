package org.ndx.codingame.fall2026.entities;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Units implements ConstructableInUnitTest  {
	public Units(int mine, int enemies) {
		super();
		this.mine = mine;
		this.enemies = enemies;
	}
	public final int mine;
	public final int enemies;

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		return new StringBuilder("new ").append(getClass().getSimpleName()).append("(")
				.append(mine).append(", ")
				.append(enemies).append(")")
				;
	}
}
