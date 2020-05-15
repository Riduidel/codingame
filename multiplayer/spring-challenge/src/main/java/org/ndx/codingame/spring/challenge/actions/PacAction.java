package org.ndx.codingame.spring.challenge.actions;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Content;

public interface PacAction extends Action {

	AbstractPac transform(ImmutablePlayground<Content> playground);

	PacAction withMessage(String message);
	
	String message();
}
