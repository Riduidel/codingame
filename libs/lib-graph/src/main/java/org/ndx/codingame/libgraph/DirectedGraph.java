package org.ndx.codingame.libgraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class DirectedGraph implements Graph {
	private final Map<Integer, Vertex> vertices = new TreeMap<>();
	private final Collection<Edge> edges = new ArrayList<>();

	public boolean hasVertex(final int id) {
		return vertices.containsKey(id);
	}
	public Vertex getVertex(final int id) {
		return vertices.get(id);
	}
	public Vertex createVertex(final int id) {
		final Vertex vertex = new Vertex(id);
		vertices.put(id, vertex);
		return vertex;
	}
	@Override
	public Vertex getOrCreateVertex(final int id) {
		if(hasVertex(id)) {
			return getVertex(id);
		} else {
			return createVertex(id);
		}
	}
	@Override
	public Edge getOrCreateEdgeBetween(final int from, final int to) {
		return getOrCreateEdgeBetween(getOrCreateVertex(from), getOrCreateVertex(to));
	}
	@Override
	public Edge getOrCreateEdgeBetween(final Vertex from, final Vertex to) {
		if(hasEdgeBetween(from, to)) {
			return getEdgeBetween(from, to);
		} else {
			return createEdgeBetween(from, to);
		}
	}
	protected Edge createEdgeBetween(final Vertex from, final Vertex to) {
		final Edge returned = new Edge(from, to);
		from.putEdge(Navigator.DESTINATION, returned);
		to.putEdge(Navigator.SOURCE, returned);
		edges.add(returned);
		return returned;
	}
	private boolean hasEdgeBetween(final Vertex from, final Vertex to) {
		return from.hasEdge(Navigator.DESTINATION, to);
	}
	private Edge getEdgeBetween(final Vertex from, final Vertex to) {
		return from.getEdge(Navigator.DESTINATION, to);
	}
	@Override
	public Collection<Vertex> vertices() {
		return vertices.values();
	}
	@Override
	public Collection<Edge> edges() {
		return edges;
	}
	
	@Override
	public <Type> Type accept(final GraphVisitor<Type> visitor) {
		visitor.startVisit(this);
		for(final Vertex v : vertices()) {
			v.accept(this, visitor);
		}
		return visitor.endVisit(this);
	}
}
