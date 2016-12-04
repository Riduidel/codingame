package org.ndx.codingame.fantastic.status;

import java.util.HashMap;
import java.util.Map;

public class Status {
	private final Map<Class<? extends StatusElement>, StatusElement> elements = new HashMap<>();
	
	public void set(final StatusElement s) {
		elements.put(s.getClass(), s);
	}
	
	public <Type extends StatusElement> Type get(final Class<Type> type) {
		return type.cast(elements.get(type));
	}

	public void advanceOneTurn() {
		for(final StatusElement e : elements.values()) {
			e.advanceOneTurn();
		}
	}

	public void setCaptain(final int id) {
		set(new CaptainStatus(id));
	}

	public int getCaptain() {
		return get(CaptainStatus.class).captain;
	}

	public void setTeam(final int myTeamId) {
		set(new TeamStatus(myTeamId));
	}

	public int getTeam() {
		return get(TeamStatus.class).team;
	}

	public void setMagic(final int magic) {
		set(new MagicStatus(magic));
	}

	public int getMagic() {
		if(!elements.containsKey(MagicStatus.class)) {
			setMagic(0);
		}
		return get(MagicStatus.class).magic;
	}

	public void setIfNeeded(final StatusElement element) {
		if(!elements.containsKey(element.getClass())) {
			set(element);
		}
	}
}
