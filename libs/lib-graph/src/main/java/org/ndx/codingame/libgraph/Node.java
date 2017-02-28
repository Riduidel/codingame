package org.ndx.codingame.libgraph;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Node<NodeType extends Node<?>> {
	
	public static class ByProperty<Type extends Node<Type>> implements Comparator<Type> {
		private final GraphProperty<? extends Comparable> property;
		public ByProperty(final GraphProperty<? extends Comparable> property) {
			this.property = property;
		}
		@Override
		public int compare(final Type o1, final Type o2) {
			return o1.getProperty(property).compareTo(o2.getProperty(property));
		}
	}

	private final Map<GraphProperty<?>, Object> properties = new HashMap<>();

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

	public Map<GraphProperty<?>, ?> getProperties() {
		return Collections.unmodifiableMap(properties);
	}

	public void importProperties(final Node source) {
		for(final GraphProperty p : (Set<GraphProperty>) source.properties.keySet()) {
			properties.put(p, p.copy(source.properties.get(p)));
		}
	}
}
