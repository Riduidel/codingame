package org.ndx.codingame.the_accountant;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class EnemyTest {

	@Test
	public void we_need_two_turns_to_reach_point_at_1000() {
		Enemy e = new Enemy(1, 0, 0, 10);
		e.findTargetIn(Arrays.asList(new DataPoint(0, 1000, 0)));
		assertThat(e.getTurnsToReachTarget()).isEqualTo(2);
	}

}
