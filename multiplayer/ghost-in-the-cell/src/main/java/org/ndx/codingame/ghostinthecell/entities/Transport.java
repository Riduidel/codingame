package org.ndx.codingame.ghostinthecell.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ndx.codingame.ghostinthecell.actions.MoveTo;
import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.GraphProperty;

public class Transport {
	public static final GraphProperty<Transport> PROPERTY = new GraphProperty<>("TRANSPORT");
	public static final Comparator<Edge> ATTACK_IN_RISING_PRIORITY = new Comparator<Edge>() {

		@Override
		public int compare(final Edge o1, final Edge o2) {
			return score(o1).compareTo(score(o2));
		}

		private Double score(final Edge edge) {
			final Transport transport = Transport.of(edge);
			final Factory destination = Factory.of(edge.destination);
			return (double)destination.production/transport.distance;
		}
		
	};

	public static final Comparator<Edge> ATTACK_IN_PRIORITY = Collections.reverseOrder(ATTACK_IN_RISING_PRIORITY);
	
	public static final int getDistanceOfTransport(final Edge edge) {
		return of(edge).distance;
	}
	
	public final int distance;
	public final List<Troop> troops;
	private Bomb bomb;

	public Transport(final int distance) {
		this(distance, new ArrayList<>());
	}

	public Transport(final int distance, final List<Troop> transported) {
		this.distance = distance;
		troops = transported;
	}

	public void cleanup() {
		troops.clear();
	}

	public void add(final Troop t) {
		troops.add(t);
	}

	public static Transport of(final Edge edge) {
		return edge.getProperty(PROPERTY);
	}

	public TransportDeposit advanceOneTurn(final Factory factory) {
		final List<Troop> transported = new ArrayList<>();
		Attack resolved = new Attack(0, 0);
		for(final Troop t : troops) {
			final Troop next = t.advanceOneTurn();
			if(next.distance==0) {
				resolved = resolved.resolve(next);
			} else {
				transported.add(next);
			}
		}
		return new TransportDeposit(resolved.owner, resolved.getCount(), new Transport(distance, transported));
	}

	public MoveTo fireMoveOf(final Edge edge, final int i) {
		final MoveTo returned = new MoveTo(edge.source.id, edge.destination.id, i);
		final Factory source = Factory.of(edge.source);
		source.setCount(source.getCount()-i);
		
		Factory.of(edge.source).cleanup();
		Factory.of(edge.destination).cleanup();
		// Finally create troops
		add(new Troop(source.owner, i, distance));
		return returned;
	}

	public int getDistance() {
		return distance;
	}

	public Bomb getBomb() {
		return bomb;
	}

	public void setBomb(final Bomb bomb) {
		this.bomb = bomb;
	}

	public boolean hasBomb() {
		return bomb!=null;
	}

}
