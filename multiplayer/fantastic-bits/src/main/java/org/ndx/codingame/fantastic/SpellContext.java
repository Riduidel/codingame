package org.ndx.codingame.fantastic;

public class SpellContext {
	public static final SpellContext NO = new SpellContext(null);
	public final Entity entity;

	public boolean shouldCast() {
		return entity!=null;
	}

	public SpellContext(Entity toPetrificate) {
		this.entity = toPetrificate;
	}
}
