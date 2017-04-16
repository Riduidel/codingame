package org.ndx.codingame.libgraph;

public enum Navigator {
	/**
	 * Allow to navigate edges that ENTER a vertex
	 */
	SOURCE,
	/**
	 * Allow to navigate edges that LEAVES a vertex
	 */
	DESTINATION;

	public Vertex navigateOn(final Edge edge) {
		switch(this) {
		case SOURCE:
			return edge.source;
		case DESTINATION:
			return edge.destination;
		}
		throw new UnsupportedOperationException("non mais alors ça !");
	}
}
