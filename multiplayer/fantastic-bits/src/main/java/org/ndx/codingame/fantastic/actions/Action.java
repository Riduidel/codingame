package org.ndx.codingame.fantastic.actions;

import org.ndx.codingame.fantastic.status.FantasticStatus;

public interface Action {
	double getScore();

	boolean conflictsWith(FantasticStatus status, Action current);
	
	<Type> Type accept(ActionVisitor<Type> visitor);

	String toCommand();

	void updateStatus(FantasticStatus status);
}
