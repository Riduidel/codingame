package org.ndx.codingame.carribeancoders.playground;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.ndx.codingame.libgraph.Navigator;

public class GraphStorageTest {

	@Test public void can_create_a_valid_square_graph_storage_of_two_nodes() {
		final GraphStorage tested = new GraphStorage(2, 2);
		assertThat(tested.coordinatesMapping).isNotEmpty();
		assertThat(tested.get(0, 0).getEdges(Navigator.DESTINATION)).hasSize(2);
		assertThat(tested.get(0, 1).getEdges(Navigator.DESTINATION)).hasSize(3);
		assertThat(tested.get(1, 0).getEdges(Navigator.DESTINATION)).hasSize(3);
		assertThat(tested.get(1, 1).getEdges(Navigator.DESTINATION)).hasSize(2);
	}

	@Test public void can_create_a_valid_square_graph_storage_of_three_nodes() {
		final GraphStorage tested = new GraphStorage(3, 3);
		assertThat(tested.coordinatesMapping).isNotEmpty();
		assertThat(tested.get(0, 0).getEdges(Navigator.DESTINATION)).hasSize(2);
		assertThat(tested.get(1, 0).getEdges(Navigator.DESTINATION)).hasSize(4);
		assertThat(tested.get(2, 0).getEdges(Navigator.DESTINATION)).hasSize(3);
		assertThat(tested.get(0, 1).getEdges(Navigator.DESTINATION)).hasSize(5);
		assertThat(tested.get(1, 1).getEdges(Navigator.DESTINATION)).hasSize(6);
	}

}
