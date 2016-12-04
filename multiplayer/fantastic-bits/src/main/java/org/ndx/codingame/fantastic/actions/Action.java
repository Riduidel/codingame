package org.ndx.codingame.fantastic.actions;

import org.ndx.codingame.fantastic.status.Status;

public interface Action {
	double getScore();

	boolean conflictsWith(Status status, Action current);
	
	<Type> Type accept(ActionVisitor<Type> visitor);

	String toCommand();

	void updateStatus(Status status);
}
