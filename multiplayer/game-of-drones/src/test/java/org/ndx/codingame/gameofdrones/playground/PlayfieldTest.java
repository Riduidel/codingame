package org.ndx.codingame.gameofdrones.playground;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.ndx.codingame.gameofdrones.entities.Drone;
import org.ndx.codingame.gameofdrones.entities.Zone;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.libgaming2d.MoveToPoint;

@Ignore
public class PlayfieldTest extends Playfield {

	@Test
	public void can_derive_simple_playground() {
		Playfield tested = new Playfield(
				1,
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
		Zone z1, z2;
		Drone d1;
		final Playfield tested = new Playfield(
				1,
				Arrays.asList(
						z1 = new Zone(0, 1000, 0),
						z2 = new Zone(1000, 1000, 0)
						),
				Arrays.asList(
						d1 = new Drone(0, 0, 1, 0)
						)
				);
		assertThat(tested.computeMovesPerDrones().get(d1)).isEqualTo(moveTo(z1));
	}


	@Test
	public void can_create_move_for_two_drones() {
		Zone z1, z2;
		Drone d1, d2;
		final Playfield tested = new Playfield(
				1,
				Arrays.asList(
						z1 = new Zone(0, 1000, 0),
						z2 = new Zone(1000, 1000, 0)
						),
				Arrays.asList(
						d1 = new Drone(0, 0, 1, 0),
						d2 = new Drone(0, 0, 1, 1)
						)
				);
		final Map<Drone, MoveToPoint> computed = tested.computeMovesPerDrones();
		assertThat(computed)
			.hasSize(2)
			.containsEntry(d1, moveTo(z1))
			.containsEntry(d2, moveTo(z2))
			;
	}

	private MoveToPoint moveTo(final Zone z1) {
		return new MoveToPoint(z1.circle.center);
	}
}
