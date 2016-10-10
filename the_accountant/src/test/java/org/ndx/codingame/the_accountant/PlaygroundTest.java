package org.ndx.codingame.the_accountant;

import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class PlaygroundTest {
	@Test
	public void can_derive_playground_once() {
		Collection<DataPoint> data = new ArrayList<DataPoint>();
		DataPoint zero = new DataPoint(0, 0, 0);
		DataPoint dataPoint = new DataPoint(1, 8250.0, 4500.0);
		data.add(dataPoint);
		Enemy enemy = new Enemy(0, 8250, 9000, 10);
		enemy.findTargetIn(data);
		assertThat(enemy.target).isEqualTo(dataPoint);
		Playground tested = new Playground(data, new TreeSet<>(Arrays.asList(enemy)));
		Playground firstRound = tested.derive();
		assertThat(firstRound.enemies).isNotEmpty();
		Enemy firstRoundEnemy = firstRound.enemies.first();
		assertThat(firstRoundEnemy).isEqualTo(new Enemy(0, 8250, 9000 - 500, 10));
		assertThat(firstRoundEnemy.target).isEqualTo(dataPoint);
		assertThat(firstRoundEnemy.distance).isEqualTo(4000);
	}

	@Test
	public void can_derive_playground_long_enough_to_eliminate_target() {
		Collection<DataPoint> data = new ArrayList<DataPoint>();
		DataPoint zero = new DataPoint(0, 0, 0);
		DataPoint dataPoint = new DataPoint(1, 8250.0, 4500.0);
		data.add(dataPoint);
		Enemy enemy = new Enemy(0, 8250, 9000, 10);
		enemy.findTargetIn(data);
		assertThat(enemy.getTurnsToReachTarget()).isEqualTo(9);
		assertThat(enemy.target).isEqualTo(dataPoint);
		Playground source = new Playground(data, new TreeSet<>(Arrays.asList(enemy)));
		Playground tested = source;
		for (int i = 0; i < 9; i++) {
			tested = tested.derive();
		}
		assertThat(tested.enemies).isNotEmpty();
		Enemy firstRoundEnemy = tested.enemies.first();
		assertThat(firstRoundEnemy).isEqualTo(new Enemy(0, 8250, 4500, 10));
		assertThat(firstRoundEnemy.target).isNull();
		assertThat(firstRoundEnemy.distance).isEqualTo(Integer.MAX_VALUE);
	}
}
