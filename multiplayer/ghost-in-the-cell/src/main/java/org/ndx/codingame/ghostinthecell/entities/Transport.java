package org.ndx.codingame.ghostinthecell.entities;

import java.util.Collection;
import java.util.Comparator;

import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.GraphProperty;
import org.ndx.codingame.libgraph.Node;

public class Transport extends Element {
	public static final GraphProperty<Integer> DISTANCE = new GraphProperty<>("DISTANCE");
	public static final GraphProperty<Collection<Troop>> TROOPS = new GraphProperty<>("TROOPS");
	
	public static final Comparator<Edge> BY_DISTANCE = new Node.ByProperty(DISTANCE);
	
}
