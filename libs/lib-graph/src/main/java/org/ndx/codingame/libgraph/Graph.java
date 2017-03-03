package org.ndx.codingame.libgraph;

import java.util.Collection;

public interface Graph {
	Collection<Vertex> vertices();
	
	Collection<Edge> edges();
	
	Vertex getOrCreateVertex(int factory1);

	Edge getOrCreateEdgeBetween(Vertex v1, Vertex v2);

	Edge getOrCreateEdgeBetween(int factory1, int factory2);

	public Graph clone();

	public <Type> Type accept(GraphVisitor<Type> visitor);
}
