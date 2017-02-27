package org.ndx.codingame.libgraph;

public interface VertexVisitor<Type> {

	boolean startVisit(Vertex vertex);

	Type endVisit(Vertex vertex);

}
