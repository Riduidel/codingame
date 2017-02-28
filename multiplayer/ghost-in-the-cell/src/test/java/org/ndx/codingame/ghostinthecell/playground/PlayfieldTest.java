package org.ndx.codingame.ghostinthecell.playground;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.ghostinthecell.entities.Factory;
import org.ndx.codingame.libgraph.Graph;
import org.ndx.codingame.libgraph.Vertex;

public class PlayfieldTest extends Playfield {

	@Test
	public void can_derive_a_bunch_of_times() {
		final Playfield tested = new Playfield();
		tested.connect(1, 2, 2);
		tested.setFactoryInfos(1, 1, 5, 1);
		tested.setFactoryInfos(2, 0, 5, 3);
		tested.setFactoryInfos(3, 0, 5, 3);
		tested.setFactoryInfos(4, -1, 5, 3);
		tested.setTroop(1, 2, 1, 1, 1);
		tested.setTroop(1, 2, 1, 5, 2);
		Graph derived = tested.derive(1);
		final Vertex myFactory = derived.getOrCreateVertex(1);
		assertThat(myFactory.getProperty(Factory.OWNER)).isEqualTo(1);
		assertThat(myFactory.getProperty(Factory.CYBORGS)).isEqualTo(6);
		// Easy, a neutral factory doesn't change
		final Vertex secondNeutralFactory = derived.getOrCreateVertex(3);
		assertThat(secondNeutralFactory.getProperty(Factory.OWNER)).isEqualTo(0);
		assertThat(secondNeutralFactory.getProperty(Factory.CYBORGS)).isEqualTo(5);
		// An enemy factory unaffected will quietly grow
		final Vertex enemyFactory = derived.getOrCreateVertex(4);
		assertThat(enemyFactory.getProperty(Factory.OWNER)).isEqualTo(-1);
		assertThat(enemyFactory.getProperty(Factory.CYBORGS)).isEqualTo(8);
		// Finally, an attacked neutral factory will decrease
		final Vertex firstNeutralFactory = derived.getOrCreateVertex(2);
		assertThat(firstNeutralFactory.getProperty(Factory.OWNER)).isEqualTo(0);
		assertThat(firstNeutralFactory.getProperty(Factory.CYBORGS)).isEqualTo(4);
		// Second attack wave !
		// Finally, an attacked neutral factory may also be won
		derived = tested.derive(2);
		final Vertex myNewFactory = derived.getOrCreateVertex(2);
		assertThat(myNewFactory.getProperty(Factory.OWNER)).isEqualTo(1);
		assertThat(myNewFactory.getProperty(Factory.CYBORGS)).isEqualTo(1);
	}

}
