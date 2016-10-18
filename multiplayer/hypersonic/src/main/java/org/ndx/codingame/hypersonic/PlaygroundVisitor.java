package org.ndx.codingame.hypersonic;

public interface PlaygroundVisitor<Type> {

	void startVisit(Playground playground);

	void startVisitRow(int y);

	void endVisitRow(int y);

	Type endVisit(Playground playground);

	void visit(int x, int y, Content content);
	
}