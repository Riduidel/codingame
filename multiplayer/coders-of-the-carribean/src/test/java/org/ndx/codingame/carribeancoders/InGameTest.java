package org.ndx.codingame.carribeancoders;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.ndx.codingame.carribeancoders.actions.MoveTo;
import org.ndx.codingame.carribeancoders.actions.ShootAt;
import org.ndx.codingame.carribeancoders.entities.Barrel;
import org.ndx.codingame.carribeancoders.entities.Cannonball;
import org.ndx.codingame.carribeancoders.entities.Entity;
import org.ndx.codingame.carribeancoders.entities.Mine;
import org.ndx.codingame.carribeancoders.entities.Ship;
import org.ndx.codingame.carribeancoders.playground.Playfield;

public class InGameTest {

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_moves_1492189151862() {
		final List<Entity> entities=new ArrayList<>();
			entities.add(new Barrel(2, 19, 12));
			entities.add(new Barrel(2, 1, 12));
			entities.add(new Barrel(13, 14, 16));
			entities.add(new Barrel(13, 6, 16));
			entities.add(new Barrel(6, 12, 10));
			entities.add(new Barrel(6, 8, 10));
			entities.add(new Barrel(3, 10, 19));
			entities.add(new Barrel(7, 14, 16));
			entities.add(new Barrel(7, 6, 16));
			entities.add(new Barrel(7, 18, 19));
			entities.add(new Barrel(7, 2, 19));
			entities.add(new Barrel(12, 17, 13));
			entities.add(new Barrel(12, 3, 13));
			entities.add(new Barrel(16, 11, 10));
			entities.add(new Barrel(16, 9, 10));
			entities.add(new Barrel(3, 19, 14));
			entities.add(new Barrel(3, 1, 14));
		final Playfield playfield = new Playfield();
		playfield.addAllEntities(entities);
		// No boats, no actions !
		assertThat(playfield.computeMoves()).isEmpty();
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_moves_1492189684380() {
		final List<Entity> entities=new ArrayList<>();
			entities.add(new Ship(19, 6, 0, 0, 100, 1));
			entities.add(new Ship(19, 14, 0, 0, 100, 0));
			entities.add(new Barrel(2, 11, 11));
			entities.add(new Barrel(2, 9, 11));
			entities.add(new Barrel(10, 11, 20));
			entities.add(new Barrel(10, 9, 20));
			entities.add(new Barrel(20, 18, 19));
			entities.add(new Barrel(20, 2, 19));
			entities.add(new Barrel(9, 13, 15));
			entities.add(new Barrel(9, 7, 15));
			entities.add(new Barrel(15, 14, 19));
			entities.add(new Barrel(15, 6, 19));
			entities.add(new Barrel(16, 15, 11));
			entities.add(new Barrel(16, 5, 11));
			entities.add(new Barrel(9, 12, 14));
			entities.add(new Barrel(9, 8, 14));
			entities.add(new Barrel(17, 15, 20));
			entities.add(new Barrel(17, 5, 20));
			entities.add(new Barrel(9, 14, 19));
			entities.add(new Barrel(9, 6, 19));
		final Playfield playfield = new Playfield();
		playfield.addAllEntities(entities);
		assertThat(playfield.movesToCommand()).isEqualTo("MOVE 19 14");
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_find_moves_with_no_barrel() {
		Ship enemy;
		final List<Entity> entities=new ArrayList<>();
			entities.add(new Ship(5, 6, 0, 1, 95, 1));
			entities.add(enemy = new Ship(12, 4, 2, 1, 100, 0));
			entities.add(new Mine(3, 7));
			entities.add(new Mine(5, 8));
			entities.add(new Cannonball(2, 6, 1, 2));
		final Playfield playfield = new Playfield();
		playfield.addAllEntities(entities);
		assertThat(playfield.computeMoves())
			.isNotEmpty()
			.contains(new MoveTo(enemy.position));
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void should_shoot_enemy_when_nearby() {
		Ship enemy;
		final List<Entity> entities=new ArrayList<>();
			entities.add(new Ship(19, 5, 1, 0, 37, 1));
			entities.add(enemy = new Ship(19, 1, 5, 0, 4, 0));
		final Playfield playfield = new Playfield();
		playfield.addAllEntities(entities);
		assertThat(playfield.computeMoves()).isNotEmpty()
			.contains(new ShootAt(enemy.position));
	}
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void should_shoot_enemy_when_both_are_stopped() {
		Ship enemy;
		final List<Entity> entities=new ArrayList<>();
			entities.add(new Ship(16, 17, 2, 0, 70, 1));
			entities.add(enemy = new Ship(15, 18, 0, 0, 17, 0));
			entities.add(new Cannonball(16, 20, 0, 0));
			entities.add(new Barrel(11, 4, 20));
			entities.add(new Barrel(18, 17, 15));
			entities.add(new Barrel(15, 5, 19));
			entities.add(new Barrel(10, 2, 14));
		final Playfield playfield = new Playfield();
		playfield.addAllEntities(entities);
		assertThat(playfield.computeMoves()).isNotEmpty()
			.contains(new ShootAt(enemy.position));
	}
}
