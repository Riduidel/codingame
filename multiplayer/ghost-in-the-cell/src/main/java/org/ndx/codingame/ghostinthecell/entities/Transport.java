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

		private Float score(final Edge edge) {
			final float production = edge.destination.getProperty(Factory.PRODUCTION);
			final float distance = edge.getProperty(Transport.DISTANCE);
			/* with linear values, it gives the following array
(D)|____ production_____|
	1		2		3
1	1		2		3
2	0,50	1,00	1,50
3	0,33	0,67	1,00
4	0,25	0,50	0,75
5	0,20	0,40	0,60
6	0,17	0,33	0,50
7	0,14	0,29	0,43
8	0,13	0,25	0,38
9	0,11	0,22	0,33

while quadratic production gives

(D)|____ production_____|
	1		2		3
1	1,00	4,00	9,00
2	0,50	2,00	4,50
3	0,33	1,33	3,00
4	0,25	1,00	2,25
5	0,20	0,80	1,80
6	0,17	0,67	1,50
7	0,14	0,57	1,29
8	0,13	0,50	1,13
9	0,11	0,44	1,00

			 */
			// we will try with linear production gain ...
			return production/distance;
		}
		
	};
}
