package org.ndx.codingame.ghostinthecell.playground;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.ToUnitTest;
import org.ndx.codingame.ghostinthecell.entities.Properties;
import org.ndx.codingame.ghostinthecell.entities.Troop;
import org.ndx.codingame.libgraph.DirectedGraph;
import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.Graph;
import org.ndx.codingame.libgraph.GraphVisitor;
import org.ndx.codingame.libgraph.Vertex;

public class Playfield {
	private final Graph graph = new DirectedGraph();

	public void connect(final int factory1, final int factory2, final int distance) {
        graph.getOrCreateEdgeBetween(factory1, factory2)
	    	.setProperty(Properties.DISTANCE, distance)
	    	.setProperty(Properties.TROOPS, new HashSet<>());
        graph.getOrCreateEdgeBetween(factory2, factory1)
	    	.setProperty(Properties.DISTANCE, distance)
	    	.setProperty(Properties.TROOPS, new HashSet<>());
	}

	public void setFactoryInfos(final int factoryId, final int owner, final int cyborgs, final int production) {
    	graph.getOrCreateVertex(factoryId)
    		.setProperty(Properties.OWNER, owner)
    		.setProperty(Properties.CYBORGS, cyborgs)
    		.setProperty(Properties.PRODUCTION, production);
	}
	
	public void cleanup() {
		for(final Vertex v : graph.vertices()) {
			v
    		.setProperty(Properties.OWNER, 0)
    		.setProperty(Properties.CYBORGS, 0)
    		.setProperty(Properties.PRODUCTION, 0);
			for(final Edge edge : v.edges()) {
				edge.getProperty(Properties.TROOPS).clear();
			}
		}
	}

	public void setTroop(final int from, final int to, final int owner, final int size, final int distance) {
    	final Edge link = graph.getOrCreateEdgeBetween(from, to);
    	final Troop t = new Troop(owner, size, distance);
    	link.getProperty(Properties.TROOPS).add(t);
	}

	public String toDebugString() {
		final StringBuilder returned = new StringBuilder();
		
		returned.append(ToUnitTest.METHOD_PREFIX+"// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)\n");
		returned.append(ToUnitTest.METHOD_PREFIX+"@Test public void can_find_move_")
			.append(System.currentTimeMillis()).append("() {\n");
		returned.append(graph.accept(new ToDebugStringVisitor()));
		returned.append(ToUnitTest.METHOD_PREFIX+"}\n\n");
		return returned.toString();
	}
	
	private static class ToDebugStringVisitor implements GraphVisitor<String> {
		private final StringBuilder returned = new StringBuilder();

		@Override
		public void startVisit(final DirectedGraph directedGraph) {
			returned.append(ToUnitTest.CONTENT_PREFIX+"Playfield tested = new Playfield();\n");
		}

		@Override
		public String endVisit(final DirectedGraph directedGraph) {
			returned.append("\n");
			return returned.toString();
		}

		@Override
		public boolean startVisit(final Vertex vertex) {
			returned.append(ToUnitTest.CONTENT_PREFIX).append("tested.setFactoryInfos(")
				.append(vertex.id).append(", ")
				.append(vertex.getProperty(Properties.OWNER)).append(", ")
				.append(vertex.getProperty(Properties.CYBORGS)).append(", ")
				.append(vertex.getProperty(Properties.PRODUCTION)).append(");\n");
			return true;
		}

		@Override
		public void endVisit(final Vertex vertex) {}

		@Override
		public boolean startVisit(final Edge value) {
			returned.append(ToUnitTest.CONTENT_PREFIX).append("tested.connect(")
				.append(value.source.id).append(", ")
				.append(value.destination.id).append(", ")
				.append(value.getProperty(Properties.DISTANCE)).append(");\n");
			for(final Troop t : value.getProperty(Properties.TROOPS)) {
				returned.append(ToUnitTest.CONTENT_PREFIX).append("tested.setTroop(")
					.append(value.source.id).append(", ")
					.append(value.destination.id).append(", ")
					.append(t.owner).append(", ")
					.append(t.count).append(", ")
					.append(t.distance).append(", ")
					.append(");\n");
				
			}
			return false;
		}

		@Override
		public void endVisit(final Edge value) {}
	}

	/** Just push troops to nearest non-owned factory */
	public String compute() {
		final Map<Integer, List<Edge>> interesting = graph.edges().stream()
			.filter((edge) -> edge.source.getProperty(Properties.OWNER)==1)
			.filter((edge) -> edge.source.getProperty(Properties.CYBORGS)>1)
			.filter((edge) -> edge.destination.getProperty(Properties.OWNER)==0)
			.collect(Collectors.groupingBy((edge) -> edge.getProperty(Properties.DISTANCE)));
		if(interesting.isEmpty()) {
			final Map<Integer, List<Edge>> enemies = graph.edges().stream()
					.filter((edge) -> edge.source.getProperty(Properties.OWNER)==1)
					.filter((edge) -> edge.source.getProperty(Properties.CYBORGS)>1)
					.filter((edge) -> edge.destination.getProperty(Properties.OWNER)<0)
					.collect(Collectors.groupingBy((edge) -> edge.destination.getProperty(Properties.CYBORGS)));
			return sendTroopsToFirstOf(enemies);
		} else {
			return sendTroopsToFirstOf(interesting);
		}
	}

	String sendTroopsToFirstOf(final Map<Integer, List<Edge>> interesting) {
		final SortedSet<Integer> ordered = new TreeSet<>(interesting.keySet());
		final Integer nearest = ordered.first();
		final Edge candidate = interesting.get(nearest).get(0);
		final Collection<Troop> troops = candidate.getProperty(Properties.TROOPS);
		final List<Troop> myTroops = troops.stream()
				.filter((troop) -> troop.owner==1)
				.collect(Collectors.toList())
				;
		if(myTroops.isEmpty()) {
			final int toMove = candidate.source.getProperty(Properties.CYBORGS)/2;
			return String.format("MOVE %d %d %d", candidate.source.id, candidate.destination.id, toMove);
		} else {
			return "WAIT";
		}
	}
}
