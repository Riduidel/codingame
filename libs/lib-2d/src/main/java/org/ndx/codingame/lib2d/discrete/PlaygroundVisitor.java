package org.ndx.codingame.lib2d.discrete;

public interface PlaygroundVisitor<Returned, Content> {

	void startVisit(Playground<Content> playground);

	void startVisitRow(int y);

	void endVisitRow(int y);

	Returned endVisit(Playground<Content> playground);

	void visit(int x, int y, Content content);
	
}