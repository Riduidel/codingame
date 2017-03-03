package org.ndx.codingame.ghostinthecell.playground;

import java.util.stream.Stream;

import org.ndx.codingame.ghostinthecell.actions.Action;
import org.ndx.codingame.libgraph.Vertex;

@FunctionalInterface
public interface MoveComputer {
	public Stream<Action> computeMovesOf(final Vertex vertex);

}
