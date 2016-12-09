package org.ndx.codingame.hypersonic.playground;

import org.ndx.codingame.hypersonic.entities.Content;

public interface PlaygroundVisitor<Type> {

	void startVisit(Playfield playground);

	void startVisitRow(int y);

	void endVisitRow(int y);

	Type endVisit(Playfield playground);

	void visit(int x, int y, Content content);
	
}