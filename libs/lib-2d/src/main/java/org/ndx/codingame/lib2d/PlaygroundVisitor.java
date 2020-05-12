package org.ndx.codingame.lib2d;

public interface PlaygroundVisitor<Returned, Content> {

	void startVisit(ImmutablePlayground<Content> playground);

	void startVisitRow(int y);

	void endVisitRow(int y);

	Returned endVisit(ImmutablePlayground<Content> playground);

	void visit(int x, int y, Content content);
	
}