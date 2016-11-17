package org.ndx.codingame.hypersonic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.ndx.codingame.hypersonic.content.Bomb;

public class GamerTest {
	@Test public void can_find_a_safe_move() {
		Playfield read = PlayerTest.read(Arrays.asList(
				"...0.", 
				".XXX.", 
				".X...", 
				"0X...", 
				"....."));
		Gamer me = new Gamer(0, 0, 0, 1, 3);
		assertThat(me.compute(read))
			.isNotEqualTo("MOVE 0 0")
			.isIn("MOVE 1 0", "MOVE 0 1");
	}
	
	@Test public void can_find_a_good_move() {
		Playfield read = PlayerTest.read(Arrays.asList(
				"...0.", 
				".XXX.", 
				".X...", 
				"0X...", 
				"....."));
		Gamer me = new Gamer(0, 0, 1, 1, 3);
		assertThat(me.compute(read))
			.isEqualTo("BOMB 0 0");
	}
	
	@Test public void can_avoid_dead() {
		Playfield read = PlayerTest.read(Arrays.asList(
				"...0.", 
				".....", 
				".X...", 
				".X...", 
				"....."));
		Gamer me = new Gamer(0, 1, 0, 0, 3);
		read.readGameEntities(new Bomb(0, 2, 0, 2, 3));
		assertThat(me.compute(read))
			.isEqualTo("MOVE 1 1");
	}
	
}