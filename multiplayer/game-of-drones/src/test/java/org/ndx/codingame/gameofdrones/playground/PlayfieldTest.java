package org.ndx.codingame.gameofdrones.playground;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.ndx.codingame.gameofdrones.entities.Drone;
import org.ndx.codingame.gameofdrones.entities.Zone;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.libgaming2d.MoveToPoint;

public class PlayfieldTest extends Playfield {

	@Test
	public void can_derive_simple_playground() {
		Playfield tested = new Playfield(
				Arrays.asList(
						new Zone(1000, 1000, 0)
						), 
				Arrays.asList(
						new Drone(0, 0, 10, 10, 1, 0),
						new Drone(0, 0, 0, 0, 1, 1),
						new Drone(0, 0, 9, 9, 0, 0),
						new Drone(0, 0, 9, 9, 0, 1)
						)
				);
		for (int i = 0; i < 200; i++) {
			tested = tested.derive();
			final Zone zone = tested.getZones().get(0);
			final Drone moving = tested.getDrones().get(0);
			final Drone staying = tested.getDrones().get(1);
			final Drone defending = tested.getDrones().get(2);
			if(i<91) {
				assertThat(zone.circle.includesOrContains(moving.position)).isFalse();
				assertThat(zone.owner).isEqualTo(0);
			} else if(i<102) {
				assertThat(zone.circle.includesOrContains(moving.position)).isTrue();
				assertThat(zone.circle.includesOrContains(defending.position)).isFalse();
				assertThat(zone.owner).isEqualTo(1);
			} else {
				assertThat(zone.circle.includesOrContains(moving.position)).isTrue();
				assertThat(zone.circle.includesOrContains(defending.position)).isTrue();
			assertThat(zone.owner).isEqualTo(0);
			}
			assertThat(staying.position).isEqualTo(Geometry.at(0, 0));
		}
	}

	@Test
	public void can_create_simple_move() {
		final Playfield tested = new Playfield(
				Arrays.asList(
						new Zone(0, 1000, 0),
						new Zone(1000, 1000, 0)
						), 
				Arrays.asList(
						new Drone(0, 0, 1, 0)
						)
				);
		final Drone d1 = tested.getDrones().get(0);
		assertThat(tested.computeMovesPerDrones().get(d1)).isEqualTo(new MoveToPoint(Geometry.at(0, 1000)));
	}

}
