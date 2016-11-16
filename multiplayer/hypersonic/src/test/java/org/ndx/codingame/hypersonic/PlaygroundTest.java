package org.ndx.codingame.hypersonic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.ndx.codingame.hypersonic.content.Bomb;
import org.ndx.codingame.hypersonic.content.Box;
import org.ndx.codingame.hypersonic.content.Item;
import org.ndx.codingame.hypersonic.content.Nothing;
import org.ndx.codingame.hypersonic.content.Wall;

public class PlaygroundTest {
	@Test public void can_read_playground() {
		Playfield read = PlayerTest.read(Arrays.asList(
				".0", 
				".X", 
				"X."));
		assertThat(read.width).isEqualTo(2);
		assertThat(read.height).isEqualTo(3);
		assertThat(read.get(0, 0)).isInstanceOf(Nothing.class);
		assertThat(read.get(1, 0)).isInstanceOf(Box.class);
		assertThat(read.get(0, 2)).isInstanceOf(Wall.class);
	}

	@Test public void can_write_playground_to_physical() {
		Playfield read = PlayerTest.read(Arrays.asList(
				".0", 
				".X", 
				"X."));
		assertThat(read.toPhysicialString()).isEqualTo(
				".0\n"
				+ ".X\n"
				+ "X.");
	}

	@Test public void can_write_playground_with_game_infos_to_physical() {
		Playfield read = PlayerTest.read(Arrays.asList(
				".0", 
				".X", 
				"X."));
		read.readGameEntities(
				new Gamer(0, 0, 0, 1, 3),
				new Bomb(0, 0, 1, 2, 3),
				new Item(0, 1, 0, 1, 0)
				);
		assertThat(read.toString()).isEqualTo(
				"|G(1,3)| I(1) \n"+
				"|B(2,3)|  X   \n"+
				"|  X   |  .   ");
	}
}