package org.ndx.codingame.libgraph;

import java.util.Map;
import java.util.TreeMap;

public class Vertex extends Node<Vertex> {
	private final Map<Integer, Edge> edges = new TreeMap<>();

	public final int id;

	public Vertex(final int id) {
		this.id = id;
	}

	public Edge getEdgeTo(final Vertex to) {
		return edges.get(to.id);
	}

	public boolean hasEdgeTo(final Vertex to) {
		return edges.containsKey(to.id);
	}

	public Edge createEdgeTo(final Vertex to) {
		final Edge returned = new Edge(this, to);
		putEdge(to, returned);
		return returned;
	}

	void putEdge(final Vertex to, final Edge returned) {
		edges.put(to.id, returned);
	}

	public Iterable<Edge> edges() {
		return edges.values();
	}

	public <Type> void accept(final Graph parent, final GraphVisitor<Type> visitor) {
		if(visitor.startVisit(this)) {
			for(final Map.Entry<Integer, Edge> entry : edges.entrySet()) {
				visit(parent, parent.getOrCreateVertex(entry.getKey()), entry.getValue(), visitor);
			}
			visitor.endVisit(this);
		}
		
	}

	private <Type> void visit(final Graph parent, final Vertex destination, final Edge value, final GraphVisitor<Type> visitor) {
		if(visitor.startVisit(value)) {
			destination.accept(parent, visitor);
			visitor.endVisit(value);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (edges == null ? 0 : edges.hashCode());
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

}
