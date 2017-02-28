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

	public static final GraphProperty<Integer> HORIZON = new GraphProperty<>("horizon");
	public static final Comparator<Vertex> BY_HORIZON = new Node.ByProperty<>(HORIZON);

	public static final GraphProperty<Integer> LIFETIME= new GraphProperty<>("lifetime");
	public static final Comparator<Vertex> BY_LIFETIME = new Node.ByProperty<>(LIFETIME);
	public static final int MAX_PRODUCTION = 3;
	public static final GraphProperty<Boolean> SURVIVOR = new GraphProperty<>("SURVIVOR");
}
