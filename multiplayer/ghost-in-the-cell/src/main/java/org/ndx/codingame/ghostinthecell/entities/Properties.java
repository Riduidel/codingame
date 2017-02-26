package org.ndx.codingame.ghostinthecell.entities;

import java.util.Collection;

import org.ndx.codingame.libgraph.GraphProperty;

public class Properties {
	public static final GraphProperty<Integer> OWNER = new GraphProperty<>("OWNER");
	public static final GraphProperty<Integer> CYBORGS = new GraphProperty<>("CYBORGS");
	public static final GraphProperty<Integer> PRODUCTION = new GraphProperty<>("PRODUCTION");
	public static final GraphProperty<Collection<Troop>> TROOPS = new GraphProperty<>("TROOPS");
	public static final GraphProperty<Integer> DISTANCE = new GraphProperty<>("DISTANCE");

}
