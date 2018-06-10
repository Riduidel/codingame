package org.ndx.codingame.fantastic.status;

import org.ndx.codingame.libstatus.Status;

public class FantasticStatus extends Status {
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
		if(!containsKey(MagicStatus.class)) {
			setMagic(0);
		}
		return get(MagicStatus.class).magic;
	}
}
