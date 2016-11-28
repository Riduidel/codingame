package org.ndx.codingame.fantastic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class InGameTest {
	@Test public void can_find_actions_in_1480336610504() {
		Status status = new Status();
			status.team = 0;
			status.magic = 0;
		List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 15000, 2250, 15000, 2250, 1, false, false));
			playing.add(new Wizard(3, 15000, 5250, 15000, 5250, 1, false, false));
			playing.add(new Snaffle(4, 5285, 6874, 5285, 6874));
			playing.add(new Snaffle(5, 10715, 626, 10715, 626));
			playing.add(new Snaffle(6, 2318, 2579, 2318, 2579));
			playing.add(new Snaffle(7, 13682, 4921, 13682, 4921));
			playing.add(new Snaffle(8, 6898, 655, 6898, 655));
			playing.add(new Snaffle(9, 9102, 6845, 9102, 6845));
			playing.add(new Snaffle(10, 8000, 3750, 8000, 3750));
			playing.add(new Bludger(11, 7450, 3750, 7450, 3750));
			playing.add(new Bludger(12, 8550, 3750, 8550, 3750));
		List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 1000, 5250, 1000, 5250, 0, false, true));
			myTeam.add(new Wizard(1, 1000, 2250, 1000, 2250, 0, false, false));
		// TODO Write that test !
		for(Wizard my : myTeam) {
			my.play(status, playing, myTeam);
		}
	}

}
