package org.ndx.codingame.ghostinthecell.playground;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.ToUnitTest;
import org.ndx.codingame.ghostinthecell.actions.Action;
import org.ndx.codingame.ghostinthecell.actions.Message;
import org.ndx.codingame.ghostinthecell.entities.Bomb;
import org.ndx.codingame.ghostinthecell.entities.Bombs;
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
	private class ToDebugStringVisitor implements GraphVisitor<String> {
		private final StringBuilder returned = new StringBuilder();

		@Override
		public boolean startVisit(final Graph directedGraph) {
			returned.append(ToUnitTest.CONTENT_PREFIX+"Playfield p = new PlayfieldBuilder()")
				.append(".at(").append(turn).append(")")
				.append("\n");
			return true;
		}

		@Override
		public String endVisit(final Graph directedGraph) {
			enemyBombs.stream().forEach((e)->returned.append(".e(").append(e).append(")"));
			returned.append(";\n");
			returned.append(ToUnitTest.CONTENT_PREFIX).append("assertThat(p.compute()).isNotNull();\n");
			return returned.toString();
		}

		@Override
		public void visit(final Vertex vertex) {
			final Factory f = vertex.getProperty(Factory.PROPERTY);
			returned.append(ToUnitTest.CONTENT_PREFIX).append(".i(")
				.append(vertex.id).append(", ")
				.append(f.owner).append(", ")
				.append(f.getCount()).append(", ")
				.append(f.production).append(")\n");
			returned.append(ToUnitTest.CONTENT_PREFIX).append(".f(").append(vertex.id).append(")");

			for(final Edge edge : vertex.getEdges(Navigator.DESTINATION)) {
				final Transport t = edge.getProperty(Transport.PROPERTY);
				returned
					.append(".t(").append(edge.destination.id).append(")")
					.append(".d(").append(t.distance).append(")");
			}
			returned.append("\n");
		}

		@Override
		public void visit(final Edge value) {
			final Transport transport = Transport.of(value);
			for(final Troop t : transport.troops) {
				returned.append(ToUnitTest.CONTENT_PREFIX).append(".t(")
					.append(value.source.id).append(",")
					.append(value.destination.id).append(",")
					.append(t.owner).append(",")
					.append(t.getCount()).append(",")
					.append(t.distance)
					.append(")");
				returned.append("\n");
			}
			if(transport.hasBomb()) {
				returned.append(ToUnitTest.CONTENT_PREFIX).append(".b(")
					.append(value.source.id).append(", ")
					.append(value.destination.id).append(", ")
					.append(transport.getBomb().distance).append(")");
				returned.append("\n");
			}
		}
	}

	public final Graph graph = new DirectedGraph();
	
	final Bombs bombs;
	
	final SortedSet<Integer> enemyBombs = new TreeSet<>();

	private int turn;

	private int enemyCyborgs;

	private int myCyborgs;

	private int enemyProduction;

	private int myProduction;
	
	public Playfield() {
		this(new Bombs(2));
	}
	
	public Playfield(final Bombs bombs) {
		this.bombs = bombs;
	}
	
	public void connect(final int factory1, final int factory2, final int distance) {
        graph.getOrCreateEdgeBetween(factory1, factory2)
        	.setProperty(Transport.PROPERTY, new Transport(distance));
        graph.getOrCreateEdgeBetween(factory2, factory1)
    	.setProperty(Transport.PROPERTY, new Transport(distance));
	}

	public void setFactoryInfos(final int factoryId, final int owner, final int cyborgs, final int production) {
    	graph.getOrCreateVertex(factoryId)
    		.setProperty(Factory.PROPERTY, new Factory(owner, cyborgs, production, Factory.of(graph.getOrCreateVertex(factoryId))));
    	if(owner>0) {
			setMyCyborgs(getMyCyborgs() + cyborgs);
			myProduction += production;
		} else if(owner<0) {
			enemyCyborgs+=cyborgs;
			enemyProduction += production;
		}
	}
	
	public void cleanup() {
		setMyCyborgs(enemyCyborgs = 0);
		myProduction = enemyProduction = 0;
		enemyBombs.clear();
		for(final Vertex v : graph.vertices()) {
			v.getProperty(Factory.PROPERTY).cleanup();
			for(final Edge edge : v.getEdges(Navigator.DESTINATION)) {
				edge.getProperty(Transport.PROPERTY).cleanup();
			}
		}
	}

	public void setTroop(final int from, final int to, final int owner, final int count, final int distance) {
    	final Edge link = graph.getOrCreateEdgeBetween(from, to);
    	final Troop t = new Troop(owner, count, distance);
    	Transport.of(link).add(t);
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
		final MoveComputer computer = new StandardMoveComputer(this);
//		final MoveComputer computer = new KnapSackMoveComputer(this);
		final Collection<Action> toPerform = computer.compute();
		toPerform.add(new Message(myProduction, enemyProduction));
		if(toPerform.isEmpty()) {
			return "WAIT";
		} else {
			return toPerform.stream()
					.map((action) -> action.toCommandString())
					.collect(Collectors.joining(";"));
		}
	}
	
	public void setTurn(final int i) {
		turn = i;
	}

	public void setMyBomb(final int from, final int to, final int distance) {
    	final Edge link = graph.getOrCreateEdgeBetween(from, to);
    	final Bomb b = new Bomb(distance);
    	Transport.of(link).setBomb(b);
	}

	public void setEnemyBomb(final int timer) {
		enemyBombs.add(timer);
	}

	public int getTurn() {
		return turn;
	}

	public int getMyCyborgs() {
		return myCyborgs;
	}

	public void setMyCyborgs(final int myCyborgs) {
		this.myCyborgs = myCyborgs;
	}
}
