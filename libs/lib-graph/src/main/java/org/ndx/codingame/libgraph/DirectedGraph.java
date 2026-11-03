package org.ndx.codingame.libgraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class DirectedGraph implements Graph, ConstructableInUnitTest {
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
		final Edge returned = doCreateEdge(from, to);
		doConnect(returned, from, to);
		edges.add(returned);
		return returned;
	}
	protected void doConnect(final Edge returned, final Vertex from, final Vertex to) {
		from.putEdge(Navigator.DESTINATION, returned);
		to.putEdge(Navigator.SOURCE, returned);
	}
	protected Edge doCreateEdge(final Vertex from, final Vertex to) {
		return new Edge(from, to);
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
	public Graph clone() {
		final Graph returned = doCreateClone();
		for(final Vertex sourceVertex : vertices()) {
			final Vertex createdVertex = returned.getOrCreateVertex(sourceVertex.id);
			createdVertex.importProperties(sourceVertex);
		}
		for(final Edge sourceEdge : edges()) {
			final Edge createdEdge = returned.getOrCreateEdgeBetween(sourceEdge.source.id, sourceEdge.destination.id);
			createdEdge.importProperties(sourceEdge);
		}
		return returned;
		
	}
	protected DirectedGraph doCreateClone() {
		return new DirectedGraph();
	}
	@Override
	public <Type> Type accept(final GraphVisitor<Type> visitor) {
		if(visitor.startVisit(this)) {
			vertices().stream().forEach(v -> visitor.visit(v));
			edges().stream().forEach(e -> visitor.visit(e));
		}
		return visitor.endVisit(this);
	}
	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {

		StringBuilder returned = new StringBuilder("new ").append(getClass().getSimpleName()).append("(")
					;
		returned.append(")");
		returned.append("\n").append(multilinePrefix);
		for (Vertex vertex : vertices.values()) {
			returned.append(".v(");
			returned.append(vertex.id);
			returned.append(propertiesToUnitTest((Map<GraphProperty<?>, Object>) vertex.getProperties()));
		}
		returned.append("\n").append(multilinePrefix);
		for (Edge edge : edges) {
			returned.append(".e(");
			returned.append(edge.source.id).append(", ")
				.append(edge.destination.id);
			returned.append(propertiesToUnitTest((Map<GraphProperty<?>, Object>) edge.getProperties()));
		}
		return returned;
	}
	
	public DirectedGraph v(int id, Map.Entry<GraphProperty<?>, ?>...entries) {
		Vertex v = getOrCreateVertex(id);
		for (Map.Entry<GraphProperty<?>, ?> entry : entries) {
			v.setUntypedProperty(entry.getKey(), entry.getValue());
		}
		return this;
	}
	
	public DirectedGraph e(int sourceId, int destinationId, Map.Entry<GraphProperty<?>, ?>...entries) {
		Edge e = getOrCreateEdgeBetween(sourceId, destinationId);
		for (Map.Entry<GraphProperty<?>, ?> entry : entries) {
			e.setUntypedProperty(entry.getKey(), entry.getValue());
		}
		return this;
	}
	
	public StringBuilder propertiesToUnitTest(Map<GraphProperty<?>, Object> properties) {
		StringBuilder returned = new StringBuilder();
		for(Map.Entry<GraphProperty<?>, Object> e : properties.entrySet()) {
			returned.append(", ").append("p(\"").append(e.getKey().name).append("\", ");
			if(e.getValue() instanceof ConstructableInUnitTest) {
				returned.append(((ConstructableInUnitTest) e.getValue()).toUnitTestConstructor(""));
			} else {
				returned.append(e.getValue().toString());
			}
		}
		return returned;
	}
}
