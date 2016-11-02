package org.ndx.codingame.lib2d;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class) public class PointTest {

	private Object distance1_test_parameters() {
		return new Object[][] {
			{Geometry.at(0, 0), Geometry.at(0, 0), 0},
			{Geometry.at(0, 0), Geometry.at(1, 1), 2},
			{Geometry.at(0, 0), Geometry.at(1, 0), 1}
		};
	}
	@Test
	@Parameters(method = "distance1_test_parameters")
	public void distance_1_is_valid_for(AbstractPoint first, AbstractPoint second, double expected) {
		assertThat(first.distance1To(second)).isEqualTo(expected);
	}


	private Object distance2_test_parameters() {
		return new Object[][] {
			{Geometry.at(0, 0), Geometry.at(0, 0), 0},
			{Geometry.at(0, 0), Geometry.at(1, 1), Math.sqrt(2)},
			{Geometry.at(0, 0), Geometry.at(1, 0), 1}
		};
	}
	@Test
	@Parameters(method = "distance2_test_parameters")
	public void distance_2_is_valid_for(AbstractPoint first, AbstractPoint second, double expected) {
		assertThat(first.distance2To(second)).isEqualTo(expected);
	}
}
