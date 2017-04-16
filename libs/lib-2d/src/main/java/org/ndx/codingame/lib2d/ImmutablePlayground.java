package org.ndx.codingame.lib2d;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public interface ImmutablePlayground<Content> {
	public Content get(DiscretePoint p);

	public Content get(int x, int y);

	public boolean contains(DiscretePoint point);

	public boolean contains(int x, int y);

}
