package org.ndx.codingame.wondevwoman.playground;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.lib2d.Geometry.at;
import static org.ndx.codingame.wondevwoman.TestUtils.g;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.wondevwoman.actions.Move;
import org.ndx.codingame.wondevwoman.actions.Push;
import org.ndx.codingame.wondevwoman.entities.Floor;
import org.ndx.codingame.wondevwoman.entities.Gamer;

public class PlayfieldMoveRunnerTest {
	@Test public void can_compute_correct_move() {
		final List<Gamer> my = Arrays.asList(
				g(0, 0, 0)
				);
		final List<Gamer> enemy = Arrays.asList(
				);
		final Gaming g = Gaming.from(
				"000",
				"000",
				"00."
				);
		g.withMy(my);
		final GamingStep result = g.deriveForAction(new Move(0, "SE", "S"));
		final DiscretePoint center = at(1, 1);
		assertThat(result.myByPosition)
		.containsOnlyKeys(center)
		.containsValue(new Gamer(center, 0));
		assertThat(result.playfield.get(at(1, 2))).isEqualTo(new Floor(1));
	}

	@Test public void can_compute_correct_push() {
		final List<Gamer> my = Arrays.asList(
				g(0, 0, 0)
				);
		final List<Gamer> enemy = Arrays.asList(
				g(0, 1, 0)
				);
		final Gaming g = Gaming.from(
				"000",
				"000",
				"00."
				);
		g.withMy(my).withEnemy(enemy);
		final GamingStep result = g.deriveForAction(new Push(0, "S", "S"));
		final DiscretePoint topleft = at(0, 0);
		assertThat(result.myByPosition)
		.containsOnlyKeys(topleft)
		.containsValue(new Gamer(topleft, 0));
		final DiscretePoint bottomright = at(0, 2);
		assertThat(result.enemy)
		.containsOnlyKeys(bottomright)
		.containsValue(new Gamer(bottomright, 0));
		assertThat(result.playfield.get(bottomright)).isEqualTo(new Floor(1));
	}

}
