package org.ndx.codingame.spring.challenge.entities;

public enum Type {
	ROCK,
	PAPER,
	SCISSORS,
	DEAD;

	boolean isDangerousFor(Type type) {
		switch(this) {
		case ROCK: return type==SCISSORS;
		case PAPER: return type==ROCK;
		case SCISSORS: return type==PAPER;
		case DEAD: return false;
		default:
			throw new RuntimeException("What the hell is that? "+type);
		}
	}
}
