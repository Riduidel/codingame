package org.ndx.codingame.ghostinthecell.entities;

import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.Vertex;

public enum EdgeToVertexNavigator {
	SOURCE,
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
