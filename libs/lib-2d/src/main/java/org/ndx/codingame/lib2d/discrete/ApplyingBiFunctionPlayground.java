package org.ndx.codingame.lib2d.discrete;

import java.util.function.BiFunction;

import org.ndx.codingame.lib2d.ImmutablePlayground;

public class ApplyingBiFunctionPlayground<Returned, Content, Other> implements ImmutablePlayground<Returned> {

	private ImmutablePlayground<Content> a;
	private ImmutablePlayground<Other> b;
	private BiFunction<Content, Other, Returned> function;

	public ApplyingBiFunctionPlayground(ImmutablePlayground<Content> immutablePlayground,
			ImmutablePlayground<Other> other, BiFunction<Content, Other, Returned> function) {
		this.a = immutablePlayground;
		this.b = other;
		this.function = function;
	}

	@Override
	public int getWidth() {
		return a.getWidth();
	}

	@Override
	public int getHeight() {
		return a.getHeight();
	}

	@Override
	public Returned get(DiscretePoint p) {
		return function.apply(a.get(p), b.get(p));
	}

	@Override
	public Returned get(int x, int y) {
		return function.apply(a.get(x,y), b.get(x,y));
	}

	@Override
	public boolean contains(DiscretePoint point) {
		return a.contains(point) && b.contains(point);
	}

	@Override
	public boolean contains(int x, int y) {
		return a.contains(x, y) && b.contains(x, y);
	}

}
