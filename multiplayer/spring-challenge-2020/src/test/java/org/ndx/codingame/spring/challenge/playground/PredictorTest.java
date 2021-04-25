package org.ndx.codingame.spring.challenge.playground;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ndx.codingame.lib2d.Geometry.at;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.PlayerTest;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.Type;

public class PredictorTest {

	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_move_towards_small_pill() {
		Playfield tested = PlayerTest.read(Arrays.asList(
			"#######",
			"#    .#",
			"#######"
			));
		// Player is not speeding, and still in cooldown
		AbstractPac my_p1 = new Pac(3, 1, 1, true, Type.PAPER, 0, 1);
		tested.readGameEntities(my_p1);
		Map<Pac, PacAction> actions = tested.computeActions(32);
		PlayerTest.isMoveTo(my_p1, actions).isEqualTo(at(4, 1));
	}

}
