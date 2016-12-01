package org.ndx.codingame.fantastic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;

public class InGameTest {
	@Test @Ignore
	public void can_find_actions_in_1480530622497() {
		final Status status = new Status();
		status.setTeam(0);
		status.setMagic(1);
		final List<Entity> playing = new ArrayList<>();
		playing.add(new Wizard(2, 14908, 2369, -69, 89, 1, false, false));
		playing.add(new Wizard(3, 14910, 5130, -67, -90, 1, false, false));
		playing.add(new Snaffle(4, 3196, 1949, 0, 0));
		playing.add(new Snaffle(5, 12804, 5551, 0, 0));
		playing.add(new Snaffle(6, 2135, 3776, 0, 0));
		playing.add(new Snaffle(7, 13865, 3724, 0, 0));
		playing.add(new Snaffle(8, 6861, 4876, 0, 0));
		playing.add(new Snaffle(9, 9139, 2624, 0, 0));
		playing.add(new Snaffle(10, 8000, 3750, 0, 0));
		playing.add(new Bludger(11, 7328, 3778, -110, 25));
		playing.add(new Bludger(12, 8672, 3722, 110, -25));
		final List<Wizard> myTeam = new ArrayList<>();
		myTeam.add(new Wizard(0, 1150, 5254, 112, 3, 0, false, true));
		myTeam.add(new Wizard(1, 1144, 2290, 108, 30, 0, false, false));
		final Set<String> destination = new HashSet<>();
		// TODO Write that test !
		for (final Wizard my : myTeam) {
			final String p = my.play(status, playing, myTeam);
			assertThat(p).startsWith("MOVE");
			destination.add(p.substring(5));
		}
		assertThat(destination).hasSize(2);
	}
	@Test public void can_find_actions_in_1480538698557() {
		final Status status = new Status();
			status.setTeam(0);
			status.setMagic(33);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 8146, 6170, -4, 54, 1, false, false));
			playing.add(new Wizard(3, 6992, 6454, 330, -269, 1, true, false));
			playing.add(new Snaffle(4, 11703, 4700, 83, -20));
			playing.add(new Snaffle(5, 6322, 5580, -218, -58));
			playing.add(new Snaffle(7, 6992, 6454, 330, -269));
			playing.add(new Snaffle(8, 5976, 1490, 2, -2));
			playing.add(new Bludger(9, 2193, 6639, -656, 216));
			playing.add(new Bludger(10, 4111, 5936, -110, 503));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 3450, 6888, 688, -331, 0, false, false));
			myTeam.add(new Wizard(1, 10389, 4999, 312, -72, 0, false, true));
		assertThat(myTeam.get(1).play(status, playing, myTeam)).isEqualTo("FLIPENDO 4");
	}

}
