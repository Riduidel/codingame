package org.ndx.codingame.libgraph;

import java.util.HashMap;
import java.util.Map;

public abstract class Node<NodeType extends Node<?>> {
	private final Map<GraphProperty, Object> properties = new HashMap<>();

	public <Type> NodeType setProperty(final GraphProperty<Type> key, final Type value) {
		properties.put(key, value);
		return (NodeType) this;
	}

	public <Type> Type getProperty(final GraphProperty<Type> key) {
		return (Type) properties.get(key);
	}

	@Override
	public String toString() {
		return String.format("Node [properties=%s]", properties);
	}
}
