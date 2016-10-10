package org.ndx.codingame.lib2d;

import static java.lang.Math.sqrt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.lib2d.Geometry.at;
import static org.ndx.codingame.lib2d.Geometry.from;

import org.junit.Test;

public class CircleTest {
	public static class ContainsTest {

	@Test
	public void a_circle_contains_a_point() {
		assertThat(from(0, 0).cirleOf(2).contains(at(0, 1))).isTrue();
	}
	}

	public static class IntersectionTest {
		@Test
		public void circle_intersects_with_himself_producing_nothing() {
			Circle first = from(0, 0).cirleOf(2);
			assertThat(first.intersectionWith(first)).isEmpty();
		}
		@Test
		public void circle_intersects_with_interesecting_circle() {
			Circle first = from(0, 0).cirleOf(2);
			Circle second = from(2, 0).cirleOf(2);
			assertThat(first.intersectionWith(second))
				.containsExactly(at(1, sqrt(3)), at(1, -1*sqrt(3)));
		}
		@Test
		public void circle_intersects_with_tangeant_circles() {
			Circle first = from(0, 0).cirleOf(1);
			Circle second = from(2, 0).cirleOf(1);
			assertThat(first.intersectionWith(second))
				.containsExactly(at(1, 0));
		}
	}
}
