package org.ndx.codingame.spring.challenge.actions;

import java.util.List;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.playground.Playfield;

public interface PacAction extends Action {

	AbstractPac transform(ImmutablePlayground<Content> playground);

	PacAction withMessage(String message);
	
	String message();

	void update(Playfield playfield);

	List<DiscretePoint> path();
}
