package org.ndx.codingame.spring.challenge.actions;

import org.ndx.codingame.spring.challenge.entities.AbstractPac;

public abstract class AbstractAction implements PacAction {
	public final AbstractPac pac;
	
	protected String message = "";

	public AbstractAction(AbstractPac pac) {
		super();
		this.pac = pac;
	}
	
	public PacAction withMessage(String message) {
		this.message += message;
		return this;
	}
}
