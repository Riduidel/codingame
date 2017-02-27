package org.ndx.codingame.ghostinthecell.entities;

import java.util.Comparator;

import org.ndx.codingame.libgraph.GraphProperty;
import org.ndx.codingame.libgraph.Vertex;

public class Factory extends Element {

	public static final GraphProperty<Integer> OWNER = new GraphProperty<>("OWNER");
	public static final GraphProperty<Integer> CYBORGS = new GraphProperty<>("CYBORGS");
	public static final GraphProperty<Integer> PRODUCTION = new GraphProperty<>("PRODUCTION");
	public static final Comparator<Vertex> BY_CYBORG = new Comparator<Vertex>() {

		@Override
		public int compare(final Vertex o1, final Vertex o2) {
			return getCyborgs(o1)-getCyborgs(o2);
		}
	};
	
	public static final Comparator<Vertex> BY_PRODUCTION = new Comparator<Vertex>() {

		@Override
		public int compare(final Vertex o1, final Vertex o2) {
			return getProduction(o1)-getProduction(o2);
		}
	};
	
	public static int getOwner(final Vertex vertex) {
		return vertex.getProperty(OWNER);
	}

	public static int getCyborgs(final Vertex vertex) {
		return vertex.getProperty(CYBORGS);
	}
	
	public static int getProduction(final Vertex vertex) {
		return vertex.getProperty(PRODUCTION);
	}
}
