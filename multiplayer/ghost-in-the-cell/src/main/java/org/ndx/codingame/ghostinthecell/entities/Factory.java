package org.ndx.codingame.ghostinthecell.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.ghostinthecell.Constants;
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
			final Factory returned = new Factory(owner, count, production, previous);
			returned.previous = Factory.this;
			returned.computeState();
			return returned;
		}
		
	};
	
	private int production;
	private List<Factory> future;

	private Double teamCentrality;

	private Factory previous;

	private int order;

	private int maxProduction;

	public Factory(final int owner, final int cyborgs, final int production, final Factory factory) {
		super(owner, cyborgs);
		this.production = production;
		previous = factory;
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
		final double myMeanFactoryDistance = myFactoriesCount==0 ? 0 : myFactoriesTotalDistance/myFactoriesCount;
		final double otherMeanFactoryDistance = otherFactoriesCount==0 ? 0 : otherFactoriesTotalDistance/otherFactoriesCount;
		// Must be far from enemy factories, but near my factories
		return myMeanFactoryDistance;//-otherMeanFactoryDistance;
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
	private static List<Factory> populateFuture(final List<Factory> returned, final Factory factory, List<Transport> incoming, int horizon) {
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
			future = future.resolve(resolved, future.RESOLVER);
			if(future.isBombOver()) {
				future.production = future.getMaxProduction();
			}
			if(bombed) {
				future.setCount(Math.max(0,  future.getCount()-Math.max(10, future.getCount()/2)));
				future.production = 0;
			}
			returned.add(future);
			horizon--;
			incoming = next;
		}
		return returned;
	}

	private boolean isBombOver() {
		int duration = 0;
		Factory current= this;
		if(current.production>0) {
			return false;
		}
		while(current!=null) {
			if(current.production>0) {
				if(duration<5) {
					return false;
				} else {
					return true;
				}
			} else {
				duration++;
				current = current.previous;
			}
		}
		return false;
	}

	public Collection<Edge> attackInPriority(final Vertex vertex) {
		return vertex.getEdges(Navigator.DESTINATION).stream()
			.filter((edge) -> Factory.of(edge.destination).production>0)
//			.sorted(Transport.ATTACK_IN_PRIORITY)
			.sorted(Comparator.comparing(Transport::getDistanceOfTransport))
			.collect(Collectors.toList());
	}

	public Action upgrade(final Vertex vertex) {
		setCount(getCount()-10);
		return new Upgrade(vertex.id);
	}

	public Action dropBomb(final Edge e, final Bombs bombs) {
		bombs.dropOne();
		return new DropBomb(e.source.id, e.destination.id);
	}

	public void computeState() {
		if(previous==null) {
			maxProduction = production;
		} else {
			if(previous.maxProduction>production) {
				maxProduction = previous.maxProduction;
			} else {
				maxProduction = production;
			}
		}
	}

	public void setOrder(final int i) {
		order = i;
	}

	public int getOrder() {
		return order;
	}

	public int getMaxProduction() {
		return maxProduction;
	}

	public Vertex getNearest(final Vertex targetVertex, final Vertex vertex, final Factory my) {
		return targetVertex.getEdges(Navigator.SOURCE).stream()
			.filter((e) -> Factory.of(e.source).isMine())
			.sorted(Comparator.comparing(Transport::getDistanceOfTransport))
			.findFirst()
			.map((e) -> e.source)
			.get();
	}

	public int getDefenders() {
		if(owner==0) {
			return getCount()+1;
		} else {
			return getCount()+production+1;
		}
	}

	@Override
	public String toString() {
		return String.format("%s[production=%s]", super.toString(), production);
	}

	public int getProduction() {
		return production;
	}

	public void setProduction(final int production) {
		this.production = production;
	}
}
