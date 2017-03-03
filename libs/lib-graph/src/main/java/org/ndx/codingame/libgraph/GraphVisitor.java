package org.ndx.codingame.libgraph;

public interface GraphVisitor<Type> {
	public boolean startVisit(Graph graph);

	void visit(Edge edge);

	void visit(Vertex vertex);

	public Type endVisit(Graph graph);
}
