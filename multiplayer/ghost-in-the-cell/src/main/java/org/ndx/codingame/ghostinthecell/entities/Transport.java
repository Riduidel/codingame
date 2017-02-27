package org.ndx.codingame.ghostinthecell.entities;

import java.util.Collection;
import java.util.Comparator;

import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.GraphProperty;
import org.ndx.codingame.libgraph.Vertex;

public class Transport extends Element {

	public static final GraphProperty<Integer> DISTANCE = new GraphProperty<>("DISTANCE");
	public static final GraphProperty<Collection<Troop>> TROOPS = new GraphProperty<>("TROOPS");
	
	public static class ByCyborgOnEdge implements Comparator<Edge> {
		private final EdgeToVertexNavigator navigator;
		
		public ByCyborgOnEdge(final EdgeToVertexNavigator navigator) {
			this.navigator = navigator;
		}

		@Override
		public int compare(final Edge o1, final Edge o2) {
			final Vertex v1 = navigator.navigateOn(o1);
			final Vertex v2 = navigator.navigateOn(o2);
			return Factory.BY_CYBORG.compare(v1, v2);
		}
		
	}
	
	public static final Comparator<Edge> BY_DISTANCE = new Comparator<Edge>() {

		@Override
		public int compare(final Edge o1, final Edge o2) {
			return getDistance(o1)-getDistance(o2);
		}
	};

	public static int getDistance(final Edge edge) {
		return edge.getProperty(DISTANCE);
	}
	
	public static Collection<Troop> getTroops(final Edge edge) {
		return edge.getProperty(TROOPS);
	}
}
