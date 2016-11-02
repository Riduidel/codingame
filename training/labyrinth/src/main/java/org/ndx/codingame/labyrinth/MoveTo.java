package org.ndx.codingame.labyrinth;

import java.util.ArrayDeque;
import java.util.HashSet;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class MoveTo extends Strategy {
	DiscretePoint destination;
	HeatMap heatmap;

	public MoveTo(PlayField playground, DiscretePoint position) {
		this(playground, position, Agent.RANGE);
	}
	public MoveTo(PlayField playground, DiscretePoint position, int range) {
		this.destination = position;
		this.heatmap = new HeatMap(playground, position, range);
	}

	@Override
	protected int score(ScoredDirection scored, PlayField playground) {
		return heatmap.get(scored)*-1;
	}

	/**
	 * Extending a move to is far more complex than simply extend local directions.
	 * We have to identify all frontier points, then extend those points using new informations.
	 * @param playground
	 */
	public void extend(PlayField playground) {
		heatmap.extend(playground);
		System.err.println(String.format("Frontier of %s are points %s", this, heatmap.frontier.size()));
	}

	@Override
	public String toString() {
		return "MoveTo [destination=" + destination + "]";
	}

	@Override
	public Strategy mutate(DiscretePoint point) {
		if(destination.equals(point))
			return new Lookup();
		return super.mutate(point);
	}

	public int distanceToDestination(PlayField playground, DiscretePoint move) {
		return heatmap.get(move);
	}

	public DiscretePoint getDestination() {
		return destination;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MoveTo other = (MoveTo) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		return true;
	}
}
