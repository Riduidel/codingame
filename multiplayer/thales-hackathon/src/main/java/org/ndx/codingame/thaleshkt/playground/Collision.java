package org.ndx.codingame.thaleshkt.playground;

import org.ndx.codingame.thaleshkt.entities.AbstractEntity;

public class Collision implements Comparable<Collision>{
	public final AbstractEntity colliding;
	/**
	 * Number of turns in which collision should occur
	 */
	public final int turns;
	
	
	public Collision(AbstractEntity colliding, int turns) {
		super();
		this.colliding = colliding;
		this.turns = turns;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colliding == null) ? 0 : colliding.hashCode());
		result = prime * result + turns;
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
		Collision other = (Collision) obj;
		if (colliding == null) {
			if (other.colliding != null)
				return false;
		} else if (!colliding.equals(other.colliding))
			return false;
		if (turns != other.turns)
			return false;
		return true;
	}
	@Override
	public int compareTo(Collision o) {
		int returned = (int) Math.signum(turns-o.turns);
		if(returned==0) {
			returned = (int) Math.signum(hashCode()-o.hashCode());
		}
		return returned;
	}
}
