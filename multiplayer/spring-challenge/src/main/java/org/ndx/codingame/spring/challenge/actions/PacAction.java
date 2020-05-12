package org.ndx.codingame.spring.challenge.actions;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.spring.challenge.entities.Pac;

public interface PacAction extends Action {

	Pac transform();

	PacAction withMessage(String message);
}
