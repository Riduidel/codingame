package org.ndx.codingame.fantastic.spells;

import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.MagicStatus;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.fantastic.status.StatusElement;

public abstract class Spell {
	
	private int required;
	private String name;
	private StatusElement element;

	protected Spell(int requiredMagic, String name, StatusElement element) {
		this.required = requiredMagic;
		this.name = name;
		this.element = element;
	}

	public final SpellContext shouldCast(Status status, Wizard wizard, Entities entities) {
		if(status.get(MagicStatus.class).getMagic()>required) {
			StatusElement used = status.get(element.getClass());
			if(used==null) {
				status.set(element);
			}
			return shouldCastThat(status, wizard, entities);
		}
		return SpellContext.NO;
	}

	protected SpellContext shouldCastThat(Status status, Wizard wizard, Entities entities) {
		return SpellContext.NO;
	}

	public String cast(Status status, SpellContext context) {
		status.get(MagicStatus.class).cast(required);
		return String.format("%s %d", name, context.entity.id);
	}
}
