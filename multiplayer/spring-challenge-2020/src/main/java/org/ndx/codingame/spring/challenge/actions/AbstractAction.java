package org.ndx.codingame.spring.challenge.actions;

import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.playground.Playfield;

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
	
	public final String message() {
		return message;
	}
	/**
	 * Nothing to do, except when a move happens.
	 * And in that case, oh we have things to do !
	 */
	@Override
	public void update(Playfield playfield) {
	}
}
