package org.ndx.codingame.fantastic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;

public class InGameTest {
	@Test public void can_find_actions_in_1480421359385() {
		Status status = new Status();
			status.setTeam(0);
			status.setMagic(14);
		List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 6777, 2873, -288, 101, 1, false, false));
			playing.add(new Wizard(3, 12934, 2762, -364, 58, 1, false, false));
			playing.add(new Snaffle(4, 6375, 5211, 0, 0));
			playing.add(new Snaffle(5, 4982, 2346, -180, -12));
			playing.add(new Snaffle(6, 8991, 6025, 44, 5));
			playing.add(new Snaffle(7, 5787, 3013, -1031, 205));
			playing.add(new Snaffle(8, 8000, 3750, 0, 0));
			playing.add(new Bludger(9, 4915, 5982, 487, -114));
			playing.add(new Bludger(10, 9752, 1078, -553, 128));
		List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 8022, 4588, 194, -220, 0, false, true));
			myTeam.add(new Wizard(1, 6593, 4166, -90, 67, 0, false, false));
		// TODO Write that test !
		String move = myTeam.get(0).play(status, playing, myTeam);
		assertThat(move).contains("8000 3750");
	}

}
