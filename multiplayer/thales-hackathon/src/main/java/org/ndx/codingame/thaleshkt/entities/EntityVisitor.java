package org.ndx.codingame.thaleshkt.entities;

public interface EntityVisitor<Returned> {

	Returned visitFlag(Flag flag);

	Returned visitUFO(UFO ufo);

}
