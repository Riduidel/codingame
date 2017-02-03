package org.ndx.codingame.greatescape.actions;

public interface Action {

	Action decorateWith(String format);
	
	public String toCodingame();

}
