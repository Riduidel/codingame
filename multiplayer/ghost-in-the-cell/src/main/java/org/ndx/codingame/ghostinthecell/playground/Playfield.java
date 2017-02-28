package org.ndx.codingame.ghostinthecell.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.ComparatorChain;
import org.ndx.codingame.gaming.ToUnitTest;
import org.ndx.codingame.ghostinthecell.actions.Action;
import org.ndx.codingame.ghostinthecell.actions.MoveTo;
import org.ndx.codingame.ghostinthecell.actions.Upgrade;
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
	private static final int UPGRADE_TRESHOLD = 10;

	private static Comparator<Edge> FULL_ATTACK_SCORE = new ComparatorChain<>(
			Collections.reverseOrder(Transport.BY_INCREASING_ATTACK_SCORE),
			Collections.reverseOrder(new Edge.ByPropertyOnVertex(Navigator.DESTINATION, Factory.BY_CYBORG))
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
			returned.append(ToUnitTest.CONTENT_PREFIX).append("final String computed = tested.compute();\n");
			returned.append(ToUnitTest.CONTENT_PREFIX).append("assertThat(computed).isNotNull();\n");
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
					.append(t.distance)
					.append(");\n");
				
			}
			return false;
		}

		@Override
		public String endVisit(final Edge value) {
			return returned.toString();
		}
	}

	private static final int HORIZON = 20;

	public final Graph graph = new DirectedGraph();
	
	private final List<Graph> derivations = new ArrayList<>();
	
	public Playfield() {
		clearDerivations();
	}

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

	public void setTroop(final int from, final int to, final int owner, final int count, final int distance) {
    	final Edge link = graph.getOrCreateEdgeBetween(from, to);
    	final Troop t = new Troop(owner, count, distance);
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
	
	/** Just push troops to nearest non-owned factory */
	public String compute() {
		final List<Vertex> myFactories = findMyFactories();
		final Collection<Action> toPerform = performAllActionsOn(myFactories);
		if(toPerform.isEmpty()) {
			return "WAIT";
		} else {
			return toPerform.stream()
					.map((action) -> action.toCommandString())
					.collect(Collectors.joining(";"));
		}
	}

	private Collection<Action> performAllActionsOn(final List<Vertex> myFactories) {
		final Collection<Action> returned = new ArrayList<>();
		for(final Vertex factory : myFactories) {
			returned.addAll(performActionsOn(factory));
		}
		return returned;
	}

	private Collection<Action> performActionsOn(final Vertex factory) {
		final Collection<Edge> outgoing = factory.getEdges(Navigator.DESTINATION).stream()
				.filter(this::shouldBeAttacked)
				.sorted(FULL_ATTACK_SCORE)
				.collect(Collectors.toList());
		int cyborgs = factory.getProperty(Factory.CYBORGS);
		final Iterator<Edge> edgesIterator = outgoing.iterator();
		final Collection<Action> returned = new LinkedList<>();
		boolean attackAtLeastOnce = false;
		while(edgesIterator.hasNext() && cyborgs>1) {
			final Edge edge = edgesIterator.next();
			final Integer distance = edge.getProperty(Transport.DISTANCE);
			final Vertex futureDestination = getVertexAt(distance, edge.destination);
			final Integer enemies = futureDestination.getProperty(Factory.CYBORGS);
			if(cyborgs>enemies) {
				// only attack unproducing nodes with at least 11 cyborgs
				final Integer production = futureDestination.getProperty(Factory.PRODUCTION);
				int attackers = Math.min(enemies+1, cyborgs-1);
				if(production<1) {
					if(cyborgs<UPGRADE_TRESHOLD+enemies+1) {
						break;
					} else {
						attackers = UPGRADE_TRESHOLD+enemies+1;
					}
				}
				final MoveTo move = createMoveOn(edge, attackers);
				returned.add(move);
				cyborgs-=move.count;
				attackAtLeastOnce = true;
			}
		}
		if(!attackAtLeastOnce) {
			if(cyborgs>UPGRADE_TRESHOLD+1) {
				final Integer production = factory.getProperty(Factory.PRODUCTION);
				if(production<Factory.MAX_PRODUCTION) {
					returned.add(upgrade(factory));
				}
			}
		}
		return returned;
	}

	private boolean shouldBeAttacked(final Edge edge) {
		final Integer distance = edge.getProperty(Transport.DISTANCE);
		final Vertex futureDestination = getVertexAt(distance, edge.destination);
		return futureDestination.getProperty(Factory.OWNER)<=0;
	}

	private Vertex getVertexAt(final Integer distance, final Vertex destination) {
		return derive(distance).getOrCreateVertex(destination.id);
	}

	private MoveTo createMoveOn(final Edge edge, final int number) {
		final MoveTo returned = new MoveTo(edge.source.id, edge.destination.id, number);
		setTroop(returned.source, returned.destination, 1, returned.count, edge.getProperty(Transport.DISTANCE));
		clearDerivations();
		return returned;
	}

	private Upgrade upgrade(final Vertex factory) {
		final Upgrade returned = new Upgrade(factory.id);
		factory.setProperty(Factory.PRODUCTION, factory.getProperty(Factory.PRODUCTION)+1);
		return returned;
	}

	private void clearDerivations() {
		derivations.clear();
		derivations.add(graph);
	}

	private List<Vertex> findMyFactories() {
		return graph.vertices().stream()
				.filter((vertex) -> vertex.getProperty(Factory.OWNER)==1)
				// Immediatly check danger level on each factory
				.map(this::computeFactoryInfos)
				.sorted(Factory.BY_LIFETIME)
				.collect(Collectors.toList());
	}

	private List<Vertex> findEnemyFactories() {
		return graph.vertices().stream()
				.filter((vertex) -> vertex.getProperty(Factory.OWNER)<=0)
				// Immediatly check danger level on each factory
				.map(this::computeFactoryInfos)
				.sorted(Factory.BY_LIFETIME)
				.collect(Collectors.toList());
	}
	
	private Vertex computeFactoryInfos(final Vertex factory) {
		// First, compute horizon
		final int horizon = factory.getEdges(Navigator.SOURCE).stream()
			.filter((edge) -> edge.source.getProperty(Factory.OWNER)<0)
			.mapToInt((edge) -> edge.getProperty(Transport.DISTANCE))
			.min()
			.orElse(HORIZON);
		// Then, compute horizon
		factory.setProperty(Factory.HORIZON, horizon);
		boolean lost = false;
		// And factory lifetime
		for (int index = 0; index <= horizon; index++) {
			final Graph iteration = derive(index);
			final Vertex futureFactory = iteration.getOrCreateVertex(factory.id);
			if(futureFactory.getProperty(Factory.OWNER)<0) {
				factory.setProperty(Factory.LIFETIME, index-1);
				lost = true;
				break;
			}
		}
		if(!lost) {
			factory.setProperty(Factory.LIFETIME, horizon);
		}
		return factory;
	}

	public Graph derive(final int iteration) {
		if(derivations.size()<=iteration) {
			for (int i = derivations.size()-1; i <= iteration; i++) {
				final GraphDeriver deriver = new GraphDeriver(derivations.get(i));
				derivations.add(deriver.derive());
			}
		}
		return derivations.get(iteration);
	}
}
