package org.ndx.codingame.libgraph;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Vertex extends Node<Vertex> {
	private final Map<Integer, Edge> incoming = new TreeMap<>();
	private final Map<Integer, Edge> outgoing = new TreeMap<>();

	public final int id;

	public Vertex(final int id) {
		this.id = id;
	}


	public Collection<Edge> getEdges(final Navigator navigator) {
		switch(navigator) {
		case DESTINATION:
			return outgoing.values();
		case SOURCE:
			return incoming.values();
		}
		return null;
	}
	public Edge getEdge(final Navigator navigator, final Vertex to) {
		switch(navigator) {
		case DESTINATION:
			return outgoing.get(to.id);
		case SOURCE:
			return incoming.get(to.id);
		}
		return null;
	}

	public boolean hasEdge(final Navigator direction, final Vertex to) {
		switch(direction) {
		case DESTINATION:
			return outgoing.containsKey(to.id);
		case SOURCE:
			return incoming.containsKey(to.id);
		}
		return false;
	}

	public void putEdge(final Navigator direction, final Edge returned) {
		switch(direction) {
		case DESTINATION:
			outgoing.put(returned.destination.id, returned);
			break;
		case SOURCE:
			incoming.put(returned.source.id, returned);
			break;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (outgoing == null ? 0 : outgoing.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Vertex other = (Vertex) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("Vertex [id=%s, properties=%s]", id, super.toString());
	}
	
	public void accept(final GraphVisitor visitor) {
		visitor.visit(this);
	}
}
