package org.ndx.codingame.hypersonic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.hypersonic.PlayerTest.read;

import java.util.Arrays;

import org.junit.Test;
import org.ndx.codingame.hypersonic.content.Bomb;
import org.ndx.codingame.hypersonic.content.Item;

public class InGameTest {
	@Test public void can_find_move_1479318135242() {
		Playfield tested = read(Arrays.asList(
			".........0...",
			".............",
			".............",
			".............",
			"0............",
			".............",
			"0............",
			".............",
			".0...........",
			".............",
			"...0.0.0.0..."
			));
		Gamer me = new Gamer(0, 2, 6, 0, 6);
		tested.readGameEntities(
			new Item(0, 3, 0, 2, 0),
			new Item(0, 1, 2, 1, 0),
			new Item(0, 3, 2, 2, 0),
			new Item(0, 9, 2, 2, 0),
			new Item(0, 11, 2, 1, 0),
			new Bomb(0, 1, 4, 4, 5),
			new Bomb(0, 4, 4, 1, 5),
			new Bomb(0, 1, 6, 8, 6),
			new Item(0, 3, 8, 2, 0),
			new Gamer(1, 4, 9, 0, 6),
			new Bomb(1, 5, 9, 7, 6),
			new Bomb(1, 7, 9, 3, 6)
			);
		assertThat(me.compute(tested)).isNotEqualTo("MOVE 3 6");
	}
}