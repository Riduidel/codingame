package org.ndx.codingame.hypersonic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.hypersonic.content.Bomb;
import org.ndx.codingame.hypersonic.content.Fire;
import org.ndx.codingame.hypersonic.content.FireThenItem;
import org.ndx.codingame.hypersonic.content.Nothing;
import org.ndx.codingame.hypersonic.content.Wall;

public class BombTest {
	@Test public void can_iterate_until_explosion() {
		Playfield read = PlayerTest.read(Arrays.asList(
				".0.", 
				".X.", 
				"X.."));
		read.readGameEntities(new Bomb(0, 0, 0, 2, 3));
		assertThat(read.get(0, 0)).isInstanceOf(Bomb.class);
		read = read.next();
		assertThat(read.get(0, 0)).isInstanceOf(Bomb.class);
		assertThat(read.get(0, 1)).isInstanceOf(Nothing.class);
		assertThat(((Bomb) read.get(0, 0)).delay).isEqualTo(1);
		read = read.next();
		assertThat(read.get(0, 0)).isInstanceOf(Fire.class);
		assertThat(read.get(1, 0)).isInstanceOf(FireThenItem.class);
		assertThat(read.get(0, 1)).isInstanceOf(Fire.class);
		assertThat(read.get(0, 1)).isNotInstanceOf(FireThenItem.class);
		assertThat(read.get(1, 1)).isInstanceOf(Wall.class);
		assertThat(read.get(0, 2)).isInstanceOf(Wall.class);
	}
	
	@Test public void can_chain_explosions() {
		Playfield read = PlayerTest.read(Arrays.asList(
				".0..", 
				".X..", 
				"..0.",
				"X..."));
		read.readGameEntities(
				new Bomb(0, 0, 0, 2, 3),
				new Bomb(0, 0, 2, 5, 3));
		assertThat(read.get(0, 0)).isInstanceOf(Bomb.class);
		read = read.next();
		assertThat(read.get(0, 0)).isInstanceOf(Bomb.class);
		assertThat(((Bomb) read.get(0, 0)).delay).isEqualTo(1);
		assertThat(((Bomb) read.get(0, 2)).delay).isEqualTo(4);
		read = read.next();
		assertThat(read.get(0, 0)).isInstanceOf(Fire.class);
		assertThat(read.get(0, 1)).isInstanceOf(Fire.class);
		assertThat(read.get(1, 0)).isInstanceOf(Fire.class);
		assertThat(read.get(1, 1)).isInstanceOf(Wall.class);
		assertThat(read.get(0, 2)).isInstanceOf(Fire.class);
		assertThat(read.get(1, 2)).isInstanceOf(Fire.class);
		assertThat(read.get(2, 2)).isInstanceOf(Fire.class);
	}
	@Test public void can_chain_bombs_1475243223242() {
		Delay delay = new Delay();
		Playfield tested = PlayerTest.read(Arrays.asList(
			"....",
			".X..",
			"..0.",
			"X..."
			));
		Gamer me = new Gamer(0, 0, 0, 1, 3);
		tested.readGameEntities(
			new Bomb(0, 1, 0, 1, 3),
			new Bomb(0, 2, 0, 2, 3),
			new Bomb(0, 2, 1, 3, 3)
			);
		Playfield nextStep = tested.next();
		assertThat(nextStep.get(0, 0)).isInstanceOf(Fire.class);
		assertThat(nextStep.get(1, 0)).isInstanceOf(Fire.class);
		assertThat(nextStep.get(2, 0)).isInstanceOf(Fire.class);
		assertThat(nextStep.get(3, 0)).isInstanceOf(Fire.class);
		assertThat(nextStep.get(2, 1)).isInstanceOf(Fire.class);
		assertThat(nextStep.get(3, 1)).isInstanceOf(Fire.class);
		assertThat(nextStep.get(2, 2)).isInstanceOf(Fire.class);
	}
}