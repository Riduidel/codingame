package org.ndx.codingame.libgraph;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DirectedGraphTest extends DirectedGraph {

	@Test
	public void can_autocreate_vertices() {
		final Graph g = new DirectedGraph();
		final Vertex v1 = g.getOrCreateVertex(1);
		assertThat(v1.id).isEqualTo(1);
	}

	@Test
	public void can_autocreate_edges() {
		final Graph g = new DirectedGraph();
		final Edge edge = g.getOrCreateEdgeBetween(1, 2);
		assertThat(g.vertices()).contains(new Vertex(1), new Vertex(2));
	}
}
