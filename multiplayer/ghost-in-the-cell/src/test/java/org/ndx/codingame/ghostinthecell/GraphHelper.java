package org.ndx.codingame.ghostinthecell;

import java.util.Map.Entry;
import java.util.UUID;

import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.ndx.codingame.ghostinthecell.playground.Playfield;
import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.GraphProperty;
import org.ndx.codingame.libgraph.Vertex;

public class GraphHelper {

	public static void showGraph(final Playfield tested) {
		final Graph generated = generate(tested);
		display(generated);
	}

	static void display(final Graph generated) {
//		final FileSink output = new FileSinkGraphML();
		
//		generated.display();
	}

	private static Graph generate(final Playfield tested) {
		// we always have 2 edges linking 2 nodes for both directions
		final Graph returned = new MultiGraph("playfield");
		for(final Vertex v : tested.graph.vertices()) {
			final Node node = returned.addNode(v.id+"");
			copyNodeInElement(v, node);
		}
		// Now add edges
		for(final Edge e : tested.graph.edges()) {
			final org.graphstream.graph.Edge created = returned.addEdge(UUID.randomUUID().toString(), e.source.id+"", e.destination.id+"");
			copyNodeInElement(e, created);
		}
		return returned;
	}

	private static void copyNodeInElement(final org.ndx.codingame.libgraph.Node<?> source, final Element target) {
		for(final Entry<GraphProperty<?>, ?> element : source.getProperties().entrySet()) {
			target.addAttribute(element.getKey().name, element.getValue());
		}
	}

}
