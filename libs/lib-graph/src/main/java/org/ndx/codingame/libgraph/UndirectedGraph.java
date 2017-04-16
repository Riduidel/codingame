package org.ndx.codingame.libgraph;

public class UndirectedGraph extends DirectedGraph {
	@Override
	protected Edge createEdgeBetween(final Vertex from, final Vertex to) {
		final Edge returned = super.createEdgeBetween(from, to);
		doConnect(returned, to, from);
		return returned;
	}
	
	@Override
	protected DirectedGraph doCreateClone() {
		return new UndirectedGraph();
	}
}
