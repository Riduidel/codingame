package org.ndx.codingame.fantastic.actions.spells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.libstatus.StatusElement;

public class SpellStatus implements StatusElement {
	private Map<Entity, Integer> entities = new HashMap<>();
	
	private final int duration;

	public SpellStatus(int duration) {
		super();
		this.duration = duration;
	}

	public void applyOn(Entity entity) {
		entities.put(entity, duration);
	}

	public boolean isAppliedOn(Entity entity) {
		return entities.containsKey(entity);
	}

	@Override
	public void advanceOneTurn() {
		List<Entity> list = new ArrayList<>(entities.keySet());
		for (Entity entity : list) {
			int durationOf = entities.get(entity);
			if(durationOf>1) {
				entities.put(entity, durationOf-1);
			} else {
				entities.remove(entity);
			}
		}
	}

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		throw new UnsupportedOperationException("NOT EXPECTED TO BE USED !");
	}


}
