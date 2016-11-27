package org.ndx.codingame.fantastic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class InGameTest {
	@Test public void do_not_shoot_on_items() {
		List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 9819, 3394, 68, 260, 1, true));
			playing.add(new Wizard(3, 12947, 1564, -306, -20, 1, false));
			playing.add(new Snaffle(4, 6181, 4106, -68, -260));
			playing.add(new Snaffle(5, 9819, 3394, 68, 260));
			playing.add(new Snaffle(6, 6831, 1697, 0, 0));
			playing.add(new Snaffle(7, 9169, 5803, 0, 0));
			playing.add(new Snaffle(8, 8634, 5519, 103, -6));
			playing.add(new Snaffle(9, 7366, 1983, -103, 6));
			playing.add(new Snaffle(10, 8000, 3750, 0, 0));
			playing.add(new Bludger(11, 3626, 5101, 294, 236));
			playing.add(new Bludger(12, 12640, 2522, -280, -233));
		List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 6181, 4106, -68, -260, 0, true));
			myTeam.add(new Wizard(1, 1856, 4726, 248, -101, 0, false));
		assertThat(myTeam.get(0).play(playing)).isNotEqualTo("THROW 16001 3750 500");
	}

}
