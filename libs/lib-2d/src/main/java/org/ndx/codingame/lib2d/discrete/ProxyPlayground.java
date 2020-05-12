package org.ndx.codingame.lib2d.discrete;

import java.util.function.Function;
import java.util.function.Predicate;

import org.ndx.codingame.lib2d.ImmutablePlayground;

public class ProxyPlayground<Content> implements ImmutablePlayground<Content> {
	private final ImmutablePlayground<Content> source;
	private final Predicate<DiscretePoint> matcher;
	private final Function<DiscretePoint, Content> function;
	public ProxyPlayground(ImmutablePlayground<Content> source, Predicate<DiscretePoint> matcher,
			Function<DiscretePoint, Content> function) {
		super();
		this.source = source;
		this.matcher = matcher;
		this.function = function;
	}
	@Override
	public int getWidth() {
		return source.getWidth();
	}
	@Override
	public int getHeight() {
		return source.getHeight();
	}
	@Override
	public Content get(DiscretePoint p) {
		Content value = source.get(p);
		if(matcher.test(p)) {
			value = function.apply(p);
		}
		return value;
	}
	@Override
	public Content get(int x, int y) {
		DiscretePoint p = new DiscretePoint(x, y);
		return get(p);
	}
	@Override
	public boolean contains(DiscretePoint point) {
		return source.contains(point);
	}
	@Override
	public boolean contains(int x, int y) {
		return source.contains(x, y);
	}

}
