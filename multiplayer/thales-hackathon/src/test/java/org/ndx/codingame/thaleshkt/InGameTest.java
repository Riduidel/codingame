package org.ndx.codingame.thaleshkt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.SortedSet;

import org.junit.Test;
import org.ndx.codingame.thaleshkt.actions.Move;
import org.ndx.codingame.thaleshkt.entities.Flag;
import org.ndx.codingame.thaleshkt.entities.Gamer;
import org.ndx.codingame.thaleshkt.entities.UFO;
import org.ndx.codingame.thaleshkt.playground.Collision;
import org.ndx.codingame.thaleshkt.playground.Participant;
import org.ndx.codingame.thaleshkt.playground.Playfield;
import org.ndx.codingame.thaleshkt.playground.Side;
import org.ndx.codingame.thaleshkt.status.CanBoost;
import org.ndx.codingame.thaleshkt.status.MySide;
import org.ndx.codingame.thaleshkt.status.ThalesStatus;

public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)
	@Test public void can_sort_collisions_test_1() {
		ThalesStatus status = new ThalesStatus();
		status.set(new MySide(Side.LEFT));
		status.set(new CanBoost(0, 8));
		Playfield p = new Playfield(status);
		p.my.first = new UFO(Participant.MY, Gamer.FIRST, 980, 2638, 432, 124, 0);
		p.my.second = new UFO(Participant.MY, Gamer.SECOND, 980, 5361, 432, -124, 0);
		p.my.flag = new Flag(1000, 4955);;
		p.adversary.first = new UFO(Participant.ADVERSARY, Gamer.FIRST, 9162, 2868, -304, 331, 0);
		p.adversary.second = new UFO(Participant.ADVERSARY, Gamer.SECOND, 9001, 5468, -448, -28, 0);
		p.adversary.flag = new Flag(9000, 3045);
		Move firstMove = p.my.first.computeAttack(p);
		Move secondMove = p.my.second.computeDefense(p);
		SortedSet<Collision> collisions = p.computeCollisionsOf(firstMove);
		Iterator<Collision> collisionsIter = collisions.iterator();
		assertThat(collisions).isNotEmpty();
		assertThat(collisionsIter.next()).isEqualTo(new Collision(p.my.second, 10));
		assertThat(collisionsIter.next()).isEqualTo(new Collision(p.adversary.first, 15));
		assertThat(collisionsIter.next()).isEqualTo(new Collision(p.adversary.second, 24));
	}
	
}
