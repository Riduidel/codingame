package org.ndx.codingame.gaming.tounittest;

@FunctionalInterface
public interface ToUnitTestFiller {
	public StringBuilder build(String effectiveCommand);
}