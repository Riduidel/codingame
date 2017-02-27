package org.ndx.codingame.ghostinthecell.playground;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.ComparatorChain;
import org.ndx.codingame.gaming.ToUnitTest;
import org.ndx.codingame.ghostinthecell.entities.Factory;
import org.ndx.codingame.ghostinthecell.entities.Transport;
import org.ndx.codingame.ghostinthecell.entities.Troop;
import org.ndx.codingame.libgraph.DirectedGraph;
import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.Graph;
import org.ndx.codingame.libgraph.GraphVisitor;
import org.ndx.codingame.libgraph.Navigator;
import org.ndx.codingame.libgraph.Vertex;

public class Playfield {
	public static final Comparator<Edge> ENEMY_FACTORIES_COMPARATOR = new ComparatorChain<>(
				new Edge.ByPropertyOnVertex(Navigator.SOURCE, Factory.BY_CYBORG),
				Transport.BY_DISTANCE,
				new Edge.ByPropertyOnVertex(Navigator.DESTINATION, Factory.BY_CYBORG)
			);
	
	public static final Comparator<Edge> FREE_FACTORIES_COMPARATOR = new ComparatorChain<>(
			new Edge.ByPropertyOnVertex(Navigator.SOURCE, Factory.BY_CYBORG),
			Transport.BY_DISTANCE,
			new Edge.ByPropertyOnVertex(Navigator.DESTINATION, Factory.BY_CYBORG),
			Collections.reverseOrder(new Edge.ByPropertyOnVertex(Navigator.DESTINATION, Factory.BY_PRODUCTION))
			);
	
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
				.append(vertex.getProperty(Factory.OWNER)).append(", ")
				.append(vertex.getProperty(Factory.CYBORGS)).append(", ")
				.append(vertex.getProperty(Factory.PRODUCTION)).append(");\n");
			return true;
		}

		@Override
		public String endVisit(final Vertex vertex) {
			return returned.toString();
		}

		@Override
		public boolean startVisit(final Edge value) {
			returned.append(ToUnitTest.CONTENT_PREFIX).append("tested.connect(")
				.append(value.source.id).append(", ")
				.append(value.destination.id).append(", ")
				.append(value.getProperty(Transport.DISTANCE)).append(");\n");
			for(final Troop t : value.getProperty(Transport.TROOPS)) {
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
		public String endVisit(final Edge value) {
			return returned.toString();
		}
	}

	private final Graph graph = new DirectedGraph();

	public void connect(final int factory1, final int factory2, final int distance) {
        graph.getOrCreateEdgeBetween(factory1, factory2)
	    	.setProperty(Transport.DISTANCE, distance)
	    	.setProperty(Transport.TROOPS, new HashSet<>());
        graph.getOrCreateEdgeBetween(factory2, factory1)
	    	.setProperty(Transport.DISTANCE, distance)
	    	.setProperty(Transport.TROOPS, new HashSet<>());
	}

	public void setFactoryInfos(final int factoryId, final int owner, final int cyborgs, final int production) {
    	graph.getOrCreateVertex(factoryId)
    		.setProperty(Factory.OWNER, owner)
    		.setProperty(Factory.CYBORGS, cyborgs)
    		.setProperty(Factory.PRODUCTION, production);
	}
	
	public void cleanup() {
		for(final Vertex v : graph.vertices()) {
			v
    		.setProperty(Factory.OWNER, 0)
    		.setProperty(Factory.CYBORGS, 0)
    		.setProperty(Factory.PRODUCTION, 0);
			for(final Edge edge : v.edges()) {
				edge.getProperty(Transport.TROOPS).clear();
			}
		}
	}

	public void setTroop(final int from, final int to, final int owner, final int size, final int distance) {
    	final Edge link = graph.getOrCreateEdgeBetween(from, to);
    	final Troop t = new Troop(owner, size, distance);
    	link.getProperty(Transport.TROOPS).add(t);
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
	
	public int sortEdgesToEnemyFactories(final Edge first, final Edge second) {
		return ENEMY_FACTORIES_COMPARATOR.compare(first, second);
	}
	public int sortEdgesToFreeFactories(final Edge first, final Edge second) {
		return FREE_FACTORIES_COMPARATOR.compare(first, second);
	}
	
	/** Just push troops to nearest non-owned factory */
	public String compute() {
		final List<Edge> myOutgoingEdges = graph.edges().stream()
				.filter((edge) -> edge.source.getProperty(Factory.OWNER)==1)
				.filter((edge) -> edge.source.getProperty(Factory.CYBORGS)>1)
				.collect(Collectors.toList());
		final List<Edge> interesting = myOutgoingEdges.stream()
			.filter((edge) -> edge.destination.getProperty(Factory.OWNER)==0)
			.sorted(this::sortEdgesToFreeFactories)
			.collect(Collectors.toList());
		if(interesting.isEmpty()) {
			final List<Edge> enemies = myOutgoingEdges.stream()
					.filter((edge) -> edge.destination.getProperty(Factory.OWNER)<0)
					.sorted(this::sortEdgesToEnemyFactories)
					.collect(Collectors.toList());
			return sendTroopsToFirstOf(enemies);
		} else {
			return sendTroopsToFirstOf(interesting);
		}
	}

	String sendTroopsToFirstOf(final List<Edge> interesting) {
		for(final Edge candidate : interesting) {
			final Collection<Troop> troops = candidate.getProperty(Transport.TROOPS);
			final List<Troop> myTroops = troops.stream()
					.filter((troop) -> troop.owner==1)
					.collect(Collectors.toList())
					;
			if(myTroops.isEmpty()) {
				final int toMove = candidate.source.getProperty(Factory.CYBORGS)/2;
				return String.format("MOVE %d %d %d", candidate.source.id, candidate.destination.id, toMove);
			}
		}
		return "WAIT";
	}
}
