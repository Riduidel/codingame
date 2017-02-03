package org.ndx.codingame.greatescape.actions;

public abstract class AbstractAction implements Action{
	protected String message;
	@Override
	public Action decorateWith(final String format) {
		message = format;
		return this;
	}
}
