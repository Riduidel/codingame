package org.ndx.codingame.ghostinthecell.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.GraphProperty;
import org.ndx.codingame.libgraph.Navigator;
import org.ndx.codingame.libgraph.Node;

public class Transport extends Element {
	public static final GraphProperty<Integer> DISTANCE = new GraphProperty<>("DISTANCE");
	public static final GraphProperty<Collection<Troop>> TROOPS = new GraphProperty<Collection<Troop>>("TROOPS") {
		@Override
		public Collection<Troop> copy(final Collection<Troop> source) {
			final Collection<Troop> returned = new ArrayList<>();
			for(final Troop t : source) {
				returned.add(new Troop(t.owner, t.count, t.distance));
			}
			return returned;
		}
	};
	
	public static final Comparator<Edge> BY_DISTANCE = new Node.ByProperty(DISTANCE);

	public static final Comparator<Edge> BY_DESTINATION_LIFETIME = new Edge.ByPropertyOnVertex(Navigator.DESTINATION, Factory.BY_LIFETIME);

	// the last ones are the best ones
	public static final Comparator<Edge> BY_INCREASING_ATTACK_SCORE = new Comparator<Edge>() {

		@Override
		public int compare(final Edge o1, final Edge o2) {
			return score(o1).compareTo(score(o2));
		}

		private Integer score(final Edge edge) {
			final float production = edge.destination.getProperty(Factory.PRODUCTION);
			final float distance = edge.getProperty(Transport.DISTANCE);
			return (int) (production*production/distance);
		}
		
	};
}
