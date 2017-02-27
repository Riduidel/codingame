package org.ndx.codingame.ghostinthecell.entities;

import java.util.Comparator;

import org.ndx.codingame.libgraph.GraphProperty;
import org.ndx.codingame.libgraph.Node;
import org.ndx.codingame.libgraph.Vertex;

public class Factory extends Element {

	public static final GraphProperty<Integer> OWNER = new GraphProperty<>("OWNER");
	public static final GraphProperty<Integer> CYBORGS = new GraphProperty<>("CYBORGS");
	public static final GraphProperty<Integer> PRODUCTION = new GraphProperty<>("PRODUCTION");
	
	public static final Comparator<Vertex> BY_CYBORG = new Node.ByProperty<>(CYBORGS);
	public static final Comparator<Vertex> BY_PRODUCTION = new Node.ByProperty<>(PRODUCTION);
}
