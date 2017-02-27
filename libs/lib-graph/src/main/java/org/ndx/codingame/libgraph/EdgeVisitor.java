package org.ndx.codingame.libgraph;

public interface EdgeVisitor<Type> {

	boolean startVisit(Edge value);

	Type endVisit(Edge value);

}
