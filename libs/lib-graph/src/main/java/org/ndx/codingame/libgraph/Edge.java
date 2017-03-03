package org.ndx.codingame.libgraph;

import java.util.Comparator;

public class Edge extends Node<Edge> {
	public static class ByPropertyOnVertex implements Comparator<Edge> {
		private final Comparator<Vertex> comparator;
		private final Navigator navigator;
		public ByPropertyOnVertex(final Navigator navigator, final Comparator<Vertex> comparator) {
			super();
			this.navigator = navigator;
			this.comparator = comparator;
		}
		@Override
		public int compare(final Edge o1, final Edge o2) {
			final Vertex v1 = navigator.navigateOn(o1);
			final Vertex v2 = navigator.navigateOn(o2);
			return comparator.compare(v1, v2);
		}
	}

	public final Vertex source;
	public final Vertex destination;

	public Edge(final Vertex vertex, final Vertex to) {
		source = vertex;
		destination = to;
	}
	
	public void accept(final GraphVisitor visitor) {
		visitor.visit(this);
	}
}
