package org.ndx.codingame.hypersonic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.hypersonic.PlayerTest.read;

import java.util.Arrays;

import org.junit.Ignore;
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
			new Bomb(0, 4, 4, 2, 5),
			new Bomb(0, 1, 6, 8, 6),
			new Item(0, 3, 8, 2, 0),
			new Gamer(1, 4, 9, 0, 6),
			new Bomb(1, 5, 9, 7, 6),
			new Bomb(1, 7, 9, 3, 6)
			);
		assertThat(me.compute(tested)).isNotEqualTo("MOVE 3 6");
	}
	@Test public void can_find_move_1479328110167() {
		Playfield tested = read(Arrays.asList(
			"..00.000.00..",
			".X0X0X0X0X0X.",
			".0...000...0.",
			".X.X0X0X0X.X.",
			"000.0.0.0.000",
			"0X.X.X.X.X.X0",
			"000.0.0.0.000",
			".X.X0X0X0X.X.",
			".0...000...0.",
			".X0X0X0X0X0X.",
			"..00.000.00.."
			));
		Gamer me = new Gamer(0, 1, 0, 0, 3);
		tested.readGameEntities(
			new Bomb(0, 0, 2, 2, 3),
			new Bomb(1, 12, 7, 4, 3)
			);
		assertThat(me.compute(tested)).isEqualTo("MOVE 1 0");
	}
	@Test public void can_find_move_1479329573531() {
		Playfield tested = read(Arrays.asList(
			"..0.0.0.0.0..",
			".X0X.X0X.X0X.",
			"00.00...00.00",
			"0X.X.X.X.X.X0",
			"..00.....00..",
			".X.X.X.X.X.X.",
			"..00.....00..",
			"0X.X.X.X.X.X0",
			"00.00...00.00",
			".X0X.X0X.X0X.",
			"..0.0.0.0.0.."
			));
		Gamer me = new Gamer(1, 12, 10, 1, 3);
		tested.readGameEntities(
			new Gamer(0, 0, 0, 1, 3)
			);
		assertThat(me.compute(tested)).isNotIn("MOVE 12 10");
	}
	@Test public void can_find_move_1479372001821() {
		Playfield tested = read(Arrays.asList(
			"..00.0.0.00..",
			".X0X0X.X0X0X.",
			"0000..0..0000",
			".X0X.X.X.X0X.",
			"0.0.00000.0.0",
			".X.X.X.X.X.X.",
			"0.0.00000.0.0",
			".X0X.X.X.X0X.",
			"0000..0..0000",
			".X0X0X.X0X0X.",
			"..00.0.0....."
			));
		Gamer me = new Gamer(1, 8, 10, 1, 3);
		tested.readGameEntities(
			new Gamer(0, 1, 0, 1, 3),
			new Bomb(1, 11, 10, 2, 3)
			);
		assertThat(me.compute(tested)).isNotEqualTo("MOVE 9 10");
	}

	@Test public void can_find_move_1479376321186() {
		Playfield tested = read(Arrays.asList(
			"....00000.0..",
			".X0X.X.X.X0X.",
			"00.0000000.00",
			"0X.X0X0X0X.X0",
			"00..0.0.0..00",
			".X0X.X.X.X0X.",
			"00..0.0.0..00",
			"0X.X0X0X0X.X0",
			"00.0000000.00",
			".X0X.X.X.X0X.",
			"..0.00000...."
			));
		Gamer me = new Gamer(0, 3, 0, 1, 3);
		tested.readGameEntities(
			new Bomb(0, 1, 0, 1, 3),
			new Bomb(1, 9, 10, 4, 3)
			);
		assertThat(me.compute(tested)).isNotNull();
	}
	@Test public void should_not_drop_bomb_on_empty_ground() {
		Playfield tested = read(Arrays.asList(
				"...00...0....",
				".X0X0X.X0X.X.",
				"....0...0...0",
				".X0X.X.X.X0X.",
				"00..0...0..00",
				".X.X.X0X.X.X.",
				"00..0...0..00",
				".X0X.X.X.X0X.",
				"0...0...0...0",
				".X.X0X.X0X0X.",
				"....0...0...."
				));
			Gamer me = new Gamer(1, 11, 10, 1, 3);
			tested.readGameEntities(
				new Gamer(0, 2, 0, 1, 3),
				new Item(0, 9, 0, 1, 0),
				new Item(0, 10, 1, 1, 0),
				new Gamer(2, 12, 1, 1, 3),
				new Item(0, 0, 2, 2, 0),
				new Gamer(3, 0, 9, 1, 3),
				new Item(0, 2, 9, 1, 0),
				new Item(0, 3, 10, 1, 0),
				new Item(0, 9, 10, 1, 0)
				);
		assertThat(me.compute(tested)).doesNotStartWith("BOMB");
	}
	@Test public void bomb_that_spot() {
		Playfield tested = read(Arrays.asList(
			".............",
			".X.X.X.X.X0X.",
			".............",
			".X.X.X.X.X0X.",
			".............",
			".X.X0X.X.X.X.",
			".000.....0...",
			".X.X0X.X.X.X.",
			"....0........",
			".X.X.X.X.X.X.",
			"............."
			));
		Gamer me = new Gamer(2, 5, 8, 7, 9);
		tested.readGameEntities(
			new Item(0, 1, 2, 2, 0),
			new Bomb(1, 3, 2, 3, 10),
			new Bomb(1, 4, 2, 4, 10),
			new Bomb(1, 2, 4, 8, 12),
			new Gamer(1, 2, 5, 6, 12),
			new Bomb(0, 2, 8, 1, 7),
			new Item(0, 3, 8, 1, 0),
			new Gamer(0, 4, 10, 4, 7)
			);
		assertThat(me.compute(tested)).startsWith("BOMB");
	}

	@Test public void should_grab_item_beside() {
		Playfield tested = read(Arrays.asList(
			"..........0..",
			".X.X.X.X.X.X.",
			".......00...0",
			"0X.X.X.X.X0X0",
			"........0....",
			"0X.X.X.X.X.X0",
			"..0..........",
			"0X0X.X.X.X.X0",
			"0...000......",
			".X.X.X.X.X.X.",
			"..0..00......"
			));
		Gamer me = new Gamer(1, 8, 8, 2, 5);
		tested.readGameEntities(
			new Gamer(0, 1, 0, 1, 6),
			new Bomb(0, 2, 1, 2, 6),
			new Bomb(0, 0, 2, 5, 6),
			new Item(0, 6, 2, 1, 0),
			new Item(0, 10, 4, 1, 0),
			new Item(0, 4, 6, 1, 0),
			new Item(0, 8, 6, 1, 0),
			new Item(0, 7, 8, 2, 0)
			);
		assertThat(me.compute(tested)).isNotNull();
	}
	@Test public void i_should_drop_a_bomb() {
		Playfield tested = read(Arrays.asList(
			".............",
			".X.X.X.X.X.X.",
			".............",
			".X.X.X.X.X.X.",
			".............",
			".X.X.X.X.X.X.",
			".............",
			".X.X.X.X.X.X.",
			".............",
			".X.X.X.X.X.X.",
			".....0......."
			));
		Gamer me = new Gamer(1, 3, 10, 7, 8);
		tested.readGameEntities(
			new Item(0, 0, 3, 2, 0),
			new Item(0, 0, 5, 2, 0),
			new Item(0, 0, 7, 2, 0),
			new Item(0, 12, 7, 2, 0),
			new Gamer(0, 4, 8, 5, 9)
			);
		assertThat(me.compute(tested)).isNotNull();
	}
	
	@Test public void do_not_go_in_bomb_alley() {
		Playfield tested = read(Arrays.asList(
			".............",
			".X.X.X.X.X.X.",
			"..0..........",
			".X.X.X.X.X.X.",
			".....0.0.00..",
			".X.X.X.X.X0X.",
			".000.0.0.0...",
			".X0X0X.X0X0X.",
			".0000....00..",
			".X.X.X.X.X.X.",
			"..0..0......."
			));
		Gamer me = new Gamer(2, 6, 2, 6, 5);
		tested.readGameEntities(
			new Item(0, 2, 0, 1, 0),
			new Item(0, 3, 2, 1, 0),
			new Gamer(0, 4, 2, 5, 6),
			new Bomb(2, 8, 3, 2, 5),
			new Item(0, 11, 4, 1, 0),
			new Item(0, 10, 6, 2, 0),
			new Gamer(1, 6, 8, 2, 5),
			new Bomb(1, 8, 8, 7, 5),
			new Bomb(1, 7, 10, 4, 5)
			);
		assertThat(me.compute(tested)).isNotEqualTo("BOMB 6 3");
	}

	@Test @Ignore public void run_for_your_life_moron() {
		Playfield tested = read(Arrays.asList(
			".............",
			".X.X.X.X.X.X.",
			".............",
			".X.X.X.X.X.X.",
			".....0....0..",
			".X.X.X.X.X0X.",
			".000.0.......",
			".X0X.X.X.X0X.",
			".000......0..",
			".X.X.X.X.X.X.",
			"............."
			));
		Gamer me = new Gamer(2, 10, 6, 3, 6);
		tested.readGameEntities(
			new Gamer(0, 3, 4, 5, 8),
			new Bomb(2, 6, 4, 3, 5),
			new Bomb(2, 7, 4, 4, 5),
			new Bomb(2, 8, 4, 5, 5),
			new Item(0, 9, 4, 1, 0),
			new Item(0, 11, 4, 1, 0),
			new Bomb(0, 4, 6, 3, 8),
			new Gamer(1, 7, 6, 5, 5),
			new Bomb(2, 8, 6, 7, 5),
			new Bomb(2, 9, 6, 8, 6),
			new Item(0, 4, 7, 1, 0),
			new Bomb(1, 6, 8, 3, 5),
			new Item(0, 2, 10, 1, 0)
			);
		assertThat(me.compute(tested)).doesNotEndWith("10 6");
	}

}