package org.ndx.codingame.libgraph;

public interface GraphVisitor<Type> {

	void startVisit(DirectedGraph directedGraph);

	Type endVisit(DirectedGraph directedGraph);

	boolean startVisit(Vertex vertex);

	void endVisit(Vertex vertex);

	boolean startVisit(Edge value);

	void endVisit(Edge value);

}
