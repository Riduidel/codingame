package org.ndx.codingame.ghostinthecell.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.ghostinthecell.Constants;
import org.ndx.codingame.ghostinthecell.actions.Action;
import org.ndx.codingame.ghostinthecell.actions.DropBomb;
import org.ndx.codingame.ghostinthecell.actions.Upgrade;
import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.GraphProperty;
import org.ndx.codingame.libgraph.Navigator;
import org.ndx.codingame.libgraph.Vertex;

public class Factory extends Attack {
	
	public static Factory of(final Vertex v) {
		return v.getProperty(PROPERTY);
	}

	public static final GraphProperty<Factory> PROPERTY = new GraphProperty<>("FACTORY");

	public static final Comparator<Vertex> BY_DECREASING_DISTANCE_DIFFERENCE = new Comparator<Vertex>() {

		@Override
		public int compare(final Vertex o1, final Vertex o2) {
			return Factory.of(o1).getTeamCentrality(o1).compareTo(Factory.of(o2).getTeamCentrality(o2));
		}
		
	};
	
	private final Resolver<Factory> RESOLVER = new Resolver<Factory>() {

		@Override
		public Factory resolvedAttack(final int owner, final int count) {
			return new Factory(owner, count, production);
		}
		
	};
	
	public final int production;
	private List<Factory> future;

	private Double teamCentrality;

	public Factory(final int owner, final int cyborgs, final int production) {
		super(owner, cyborgs);
		this.production = production;
	}

	public Double getTeamCentrality(final Vertex o2) {
		if(teamCentrality==null) {
			teamCentrality = computeTeamCentrality(o2);
		}
		return teamCentrality;
	}

	private Double computeTeamCentrality(final Vertex vertex) {
		int myFactoriesCount = 0;
		double myFactoriesTotalDistance = 0;
		int otherFactoriesCount = 0;
		double otherFactoriesTotalDistance = 0;
		for(final Edge edge : vertex.getEdges(Navigator.DESTINATION)) {
			final Transport transport = Transport.of(edge);
			if(Factory.of(edge.destination).isMine()) {
				myFactoriesCount++;
				myFactoriesTotalDistance+= transport.distance;
			} else {
				otherFactoriesCount++;
				otherFactoriesTotalDistance+= transport.distance;
			}
		}
		final double myMeanFactoryDistance = myFactoriesTotalDistance/myFactoriesCount;
		final double otherMeanFactoryDistance = otherFactoriesTotalDistance/otherFactoriesCount;
		return myMeanFactoryDistance-otherMeanFactoryDistance;
	}

	public void cleanup() {
		future = null;
		teamCentrality = null;
	}

	/**
	 * Create the array of future factories for this vertex by reading all incoming troop moves.
	 * The goal is to be able to know, at each turn, the required number of cyborgs
	 * @param v
	 * @return
	 */
	public List<Factory> getFuture(final Vertex v) {
		if(future==null) {
			future = createFuture(v);
		}
		return future;
	}

	private List<Factory> createFuture(final Vertex v) {
		final List<Transport> incoming = v.getEdges(Navigator.SOURCE).stream()
				.map((edge) -> Transport.of(edge))
				.collect(Collectors.toList());
		final List<Factory> returned = new ArrayList<>();
		return populateFuture(returned, this, incoming, Constants.HORIZON);
	}

	/** Derive next factory from current one and various incoming troops */
	private static List<Factory> populateFuture(final List<Factory> returned, final Factory factory, final List<Transport> incoming, int horizon) {
		returned.add(factory);
		Factory future = factory;
		while(horizon>0) {
			final List<Transport> next = new ArrayList<>();
			Attack resolved = new Attack(0, 0);
			boolean bombed = false;
			for(final Transport t : incoming) {
				final TransportDeposit transported = t.advanceOneTurn(factory);
				next.add(transported.remaining);
				resolved = resolved.resolve(transported);
				bombed |= transported.bombing;
			}
			// Do not forget to add production if possible
			if(future.owner!=0) {
				future.setCount(future.getCount()+future.production);
			}
			if(bombed) {
				future.setCount(future.getCount()-Math.max(10, future.getCount()/2));
			}
			future = future.resolve(resolved, future.RESOLVER);
			returned.add(future);
			horizon--;
		}
		return returned;
	}

	public Collection<Edge> attackInPriority(final Vertex vertex) {
		return vertex.getEdges(Navigator.DESTINATION).stream()
			.filter((edge) -> Factory.of(edge.destination).production>0)
//			.sorted(Transport.ATTACK_IN_PRIORITY)
			.sorted(Comparator.comparing(Transport::getDistanceOfTransport))
			.collect(Collectors.toList());
	}

	public Action upgrade(final Vertex vertex) {
		return new Upgrade(vertex.id);
	}

	public Action dropBomb(final Edge e, final Bombs bombs) {
		bombs.dropOne();
		return new DropBomb(e.source.id, e.destination.id);
	}

}
