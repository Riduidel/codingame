package org.ndx.codingame.ghostinthecell.playground;

import java.util.ArrayList;
import java.util.Collection;

import org.ndx.codingame.ghostinthecell.entities.Factory;
import org.ndx.codingame.ghostinthecell.entities.Transport;
import org.ndx.codingame.ghostinthecell.entities.Troop;
import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.Graph;
import org.ndx.codingame.libgraph.Vertex;

public class GraphDeriver {

	final Graph source;

	public GraphDeriver(final Graph graph) {
		source = graph;
	}

	public Graph derive() {
		final Graph target = source.clone();
		// Now graph is cloned, change some values
		for (final Vertex vertex : target.vertices()) {
			final Integer owner = vertex.getProperty(Factory.OWNER);
			if(owner!=0) {
				final int newCount = vertex.getProperty(Factory.CYBORGS)+vertex.getProperty(Factory.PRODUCTION);
				vertex.setProperty(Factory.CYBORGS, newCount);
			}
		}
		// Now derive value of edges
		for (final Edge edge : target.edges()) {
			final Vertex destination = edge.destination;
			final Integer destinationOwner = destination.getProperty(Factory.OWNER);
			Integer destinationCyborgs = destination.getProperty(Factory.CYBORGS);
			final Collection<Troop> troops = edge.getProperty(Transport.TROOPS);
			final Collection<Troop> newTroops = new ArrayList<>();
			for (final Troop troop : troops) {
				if(troop.distance>1) {
					newTroops.add(troop.advanceOneTurn());
				} else {
					if(troop.owner==destinationOwner) {
						destinationCyborgs+=troop.count;
					} else {
						destinationCyborgs-=troop.count;
						if(destinationCyborgs<0) {
							destination.setProperty(Factory.OWNER, troop.owner);
							destinationCyborgs = -1*destinationCyborgs;
						}
					}
					destination.setProperty(Factory.CYBORGS, destinationCyborgs);
				}
			}
			edge.setProperty(Transport.TROOPS, newTroops);
		}
		return target;
	}

}
