package org.ndx.codingame.lib2d;

import static java.lang.Math.sqrt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.lib2d.Geometry.at;
import static org.ndx.codingame.lib2d.Geometry.from;

import java.util.Collection;

import org.junit.Test;

public class CircleTest {
	public static class ContainsTest {

		@Test
		public void a_circle_contains_a_point() {
			assertThat(from(0, 0).cirleOf(2).includes(at(0, 1))).isTrue();
		}
	}

	public static class IntersectionWithCircleTest {
		@Test
		public void circle_intersects_with_himself_producing_nothing() {
			Circle first = from(0, 0).cirleOf(2);
			assertThat(first.intersectionWith(first)).isEmpty();
		}

		@Test
		public void circle_intersects_with_interesecting_circle() {
			Circle first = from(0, 0).cirleOf(2);
			Circle second = from(2, 0).cirleOf(2);
			assertThat(first.intersectionWith(second)).containsExactly(at(1, sqrt(3)), at(1, -1 * sqrt(3)));
		}

		@Test
		public void circle_intersects_with_tangeant_circles() {
			Circle first = from(0, 0).cirleOf(1);
			Circle second = from(2, 0).cirleOf(1);
			assertThat(first.intersectionWith(second)).containsExactly(at(1, 0));
		}
	}

	public static class IntersectionWithLineTest {
		@Test
		public void circle_intersects_with_horizontal() {
			Circle first = from(0, 0).cirleOf(2);
			assertThat(first.intersectionWith(from(0, 0).lineTo(1, 0))).containsExactly(at(2, 0), at(-2, 0));
		}

		@Test
		public void circle_intersects_with_vertical() {
			Circle first = from(0, 0).cirleOf(2);
			assertThat(first.intersectionWith(from(0, 0).lineTo(0, 1))).containsExactly(at(0, 2), at(0, -2));
		}

		@Test
		public void circle_intersects_with_diagonal() {
			Circle first = from(0, 0).cirleOf(2);
			double sqrt_2 = sqrt(2);
			assertThat(first.intersectionWith(from(0, 0).lineTo(1, 1))).containsExactly(at(sqrt_2, sqrt_2),
					at(-sqrt_2, -sqrt_2));
		}
	}

	public static class IntersectionWithSegmentTest {
		@Test
		public void circle_intersects_with_horizontal() {
			Circle first = from(0, 0).cirleOf(2);
			// Segment is totally contained in circle, so there is no intersection
			assertThat(first.intersectionWith(from(0, 0).segmentTo(1, 0))).isEmpty();
		}
	}
}
