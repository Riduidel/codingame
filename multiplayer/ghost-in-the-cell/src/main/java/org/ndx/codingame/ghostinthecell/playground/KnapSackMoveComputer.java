package org.ndx.codingame.ghostinthecell.playground;

import java.util.stream.Stream;

import org.ndx.codingame.ghostinthecell.actions.Action;
import org.ndx.codingame.libgraph.Vertex;

/**
 * In knapsack problem, we consider
 * - capacity to be the number of cyborgs in given factory
 * - weight number of cyborg in factories to conquer (at the time we want to conquer them)
 * - value the production of given factory
 * 
 * @author ndelsaux
 *
 */
public class KnapSackMoveComputer extends AbstractMoveComputer implements MoveComputer {

	public KnapSackMoveComputer(final Playfield playfield) {
		super(playfield);
	}

	@Override
	public Stream<Action> computeMovesOf(final Vertex vertex) {
		// TODO Auto-generated method stub
		return null;
	}
}
