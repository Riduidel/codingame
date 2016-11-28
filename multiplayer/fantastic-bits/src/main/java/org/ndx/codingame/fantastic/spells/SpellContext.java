package org.ndx.codingame.fantastic.spells;

import org.ndx.codingame.fantastic.entities.Entity;

public class SpellContext {
	public static final SpellContext NO = new SpellContext(null);
	public final Entity entity;

	public boolean shouldCast() {
		return entity!=null;
	}

	public SpellContext(Entity target) {
		this.entity = target;
	}
}
