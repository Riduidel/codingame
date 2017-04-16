package org.ndx.codingame.lib2d;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public interface MutablePlayground<Content> extends ImmutablePlayground<Content>{
	public void set(final DiscretePoint p, final Content c);
	public void set(final int x, final int y, final Content c);
}
