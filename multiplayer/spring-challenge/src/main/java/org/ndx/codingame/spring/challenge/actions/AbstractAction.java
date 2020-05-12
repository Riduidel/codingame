package org.ndx.codingame.spring.challenge.actions;

import org.ndx.codingame.spring.challenge.entities.Pac;

public abstract class AbstractAction implements PacAction {
	public final Pac pac;
	
	protected String message;

	public AbstractAction(Pac pac) {
		super();
		this.pac = pac;
	}
	
	public PacAction withMessage(String message) {
		this.message = message;
		return this;
	}
}
