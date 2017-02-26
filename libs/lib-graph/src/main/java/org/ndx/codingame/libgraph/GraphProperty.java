package org.ndx.codingame.libgraph;

public class GraphProperty<Type> {
	public final String name;

	public GraphProperty(final String name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("[GraphProperty=%s]", name);
	}
}
