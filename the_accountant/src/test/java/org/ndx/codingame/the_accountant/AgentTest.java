package org.ndx.codingame.the_accountant;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
@RunWith(JUnitParamsRunner.class)
public class AgentTest {
	@Test 
	@Parameters({
		"2001, 13", 
		"2100, 12", 
		"2500, 10",
		"3000, 8",
		"4000, 5",
		"5000, 4",
		"6000, 3",
		"7000, 3",
		"8000, 2",
		"9000, 2",
		"10000, 1",
		})
	public void check_damage_value(int distance, int damage) {
		assertThat(Agent.computeDamageAt(distance)).isEqualTo(damage);
	}
}
