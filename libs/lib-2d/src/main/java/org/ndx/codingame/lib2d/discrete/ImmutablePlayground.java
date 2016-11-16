package org.ndx.codingame.lib2d.discrete;

public interface ImmutablePlayground<Content> {
	public Content get(DiscretePoint p);

	public Content get(int x, int y);

	public boolean contains(DiscretePoint point);

	public boolean contains(int x, int y);

}
