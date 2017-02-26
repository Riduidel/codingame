package org.ndx.codingame.libgraph;

public class Edge extends Node<Edge> {

	public final Vertex source;
	public final Vertex destination;

	public Edge(final Vertex vertex, final Vertex to) {
		source = vertex;
		destination = to;
	}

}
