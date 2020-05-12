package org.ndx.codingame.lib2d.discrete;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.ndx.codingame.lib2d.ImmutablePlayground;

public class ApplyingFunctionPlayground<Returned, Content> implements ImmutablePlayground<Returned> {

	private ImmutablePlayground<Content> source;
	private Function<Content, Returned> function;

	public ApplyingFunctionPlayground(ImmutablePlayground<Content> playground, Function<Content, Returned> function) {
		this.source = playground;
		this.function = function;
	}

	@Override
	public Returned get(DiscretePoint p) {
		return function.apply(source.get(p));
	}

	@Override
	public Returned get(int x, int y) {
		return function.apply(source.get(x, y));
	}

	@Override
	public boolean contains(DiscretePoint point) {
		return source.contains(point);
	}

	@Override
	public boolean contains(int x, int y) {
		return source.contains(x, y);
	}

	@Override
	public int getWidth() {
		return source.getWidth();
	}

	@Override
	public int getHeight() {
		return source.getHeight();
	}

}
