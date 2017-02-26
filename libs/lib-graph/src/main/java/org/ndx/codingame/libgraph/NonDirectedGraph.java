package org.ndx.codingame.libgraph;

public class NonDirectedGraph extends DirectedGraph implements Graph {
	@Override
	protected Edge createEdgeBetween(final Vertex from, final Vertex to) {
		final Edge returned = super.createEdgeBetween(from, to);
		// now just put edge back in "to" edges
		to.putEdge(from, returned);
		return returned;
	}
}
