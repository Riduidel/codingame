package org.ndx.codingame.libgraph;

public interface GraphVisitor<Type> extends VertexVisitor<Type>, EdgeVisitor<Type> {

	void startVisit(DirectedGraph directedGraph);

	Type endVisit(DirectedGraph directedGraph);
}
