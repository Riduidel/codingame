package org.ndx.codingame.lib2d;

import static java.lang.Math.sqrt;
import static org.assertj.core.api.Assertions.*;
import static org.ndx.codingame.lib2d.Geometry.at;
import static org.ndx.codingame.lib2d.Geometry.from;

import org.junit.Test;

public class LineTest {

	@Test public void can_compute_infos_on_horizontal_line() {
		Point first = at(0, 1);
		Line tested = from(first).lineTo(at(1, 1));
		assertThat(tested.coeffs.a).isEqualTo(0);
		assertThat(tested.coeffs.b).isEqualTo(-1);
		assertThat(tested.coeffs.c).isEqualTo(1);
		assertThat(tested.angle()).isEqualTo(0);
		assertThat(tested.contains(at(0.5, 1))).isTrue();
		assertThat(tested.distance2To(at(0, 0))).isEqualTo(1);
		assertThat(tested.project(at(0, 0))).isEqualTo(first);
		assertThat(tested.symetricOf(at(0, 0))).isEqualTo(at(0, 2));
		assertThat(tested.pointAtNTimes(2)).isEqualTo(at(2, 1));
		Point at_45_degrees = tested.pointAtAngle(first, 45, 1, PointBuilder.DEFAULT);
		assertThat(at_45_degrees.x).isEqualTo(1/sqrt(2), within(Geometry.ZERO));
		assertThat(at_45_degrees.y).isEqualTo(1+1/sqrt(2), within(Geometry.ZERO));
	}

	@Test public void can_compute_infos_on_vertical_line() {
		Line tested = from(at(1, 0)).lineTo(at(1, 1));
		assertThat(tested.coeffs.a).isEqualTo(1);
		assertThat(tested.coeffs.b).isEqualTo(0);
		assertThat(tested.coeffs.c).isEqualTo(-1);
		assertThat(tested.angle()).isEqualTo(90);
		assertThat(tested.contains(at(1, 0.5))).isTrue();
		assertThat(tested.distance2To(at(3, 1))).isEqualTo(2);
		assertThat(tested.project(at(0, 0))).isEqualTo(at(1, 0));
		assertThat(tested.symetricOf(at(0, 0))).isEqualTo(at(2, 0));
		assertThat(tested.pointAtNTimes(2)).isEqualTo(at(1, 2));
	}

	@Test public void can_compute_infos_on_vertical_reverse_line() {
		Line tested = from(at(1, 1)).lineTo(at(1, 0));
		assertThat(tested.coeffs.a).isEqualTo(-1);
		assertThat(tested.coeffs.b).isEqualTo(0);
		assertThat(tested.coeffs.c).isEqualTo(1);
		assertThat(tested.angle()).isEqualTo(-90);
	}

	@Test public void can_compute_infos_on_diagonal_line() {
		Line tested = from(at(0, 0)).lineTo(at(1, 1));
		assertThat(tested.coeffs.a).isEqualTo(1);
		assertThat(tested.coeffs.b).isEqualTo(-1);
		assertThat(tested.coeffs.c).isEqualTo(0);
		assertThat(tested.angle()).isEqualTo(45);
		assertThat(tested.contains(at(0.5, 0.5))).isTrue();
		assertThat(tested.distance2To(at(2, 0))).isCloseTo(sqrt(2), within(Geometry.ZERO));
		assertThat(tested.project(at(0, 1))).isEqualTo(at(0.5, 0.5));
		assertThat(tested.symetricOf(at(0, 1))).isEqualTo(at(1, -0));
		assertThat(tested.pointAtNTimes(2)).isEqualTo(at(2, 2));
	}

	@Test public void can_compute_infos_on_diagonal_downslope_line() {
		Line tested = from(at(0, 1)).lineTo(at(1, 0));
		assertThat(tested.coeffs.a).isEqualTo(-1);
		assertThat(tested.coeffs.b).isEqualTo(-1);
		assertThat(tested.coeffs.c).isEqualTo(1);
		assertThat(tested.angle()).isEqualTo(-45);
	}

}
