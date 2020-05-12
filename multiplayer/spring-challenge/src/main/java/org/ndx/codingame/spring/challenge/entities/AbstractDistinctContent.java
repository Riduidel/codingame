package org.ndx.codingame.spring.challenge.entities;

import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public abstract class AbstractDistinctContent extends DiscretePoint implements Content {

	public AbstractDistinctContent(int x, int y) {
		super(x, y);
	}

	public AbstractDistinctContent(AbstractPoint other) {
		super(other);
	}

}
