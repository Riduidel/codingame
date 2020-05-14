package org.ndx.codingame.lib2d;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.ndx.codingame.lib2d.discrete.ApplyingBiFunctionPlayground;
import org.ndx.codingame.lib2d.discrete.ApplyingFunctionPlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.MutableProxy;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.ProxyPlayground;

public interface ImmutablePlayground<Content> {
	public int getWidth();
	public int getHeight();
	public Content get(DiscretePoint p);
	public Content get(int x, int y);

	public boolean contains(DiscretePoint point);
	public boolean contains(int x, int y);

	public default <Returned> ImmutablePlayground<Returned> apply(Function<Content, Returned> function) {
		return apply(function, false);
	}
	public default <Returned, Other> ImmutablePlayground<Returned> apply(ImmutablePlayground<Other> other,
			BiFunction<Content, Other, Returned> function) {
		return apply(other, function, false);
	}

	public default <Returned> ImmutablePlayground<Returned> apply(Function<Content, Returned> function, boolean eager) {
		if(eager) {
			Playground returned = new Playground<>(getWidth(), getHeight());
			for (int y = 0; y < getHeight(); y++) {
				for (int x = 0; x < getWidth(); x++) {
					returned.set(x, y, function.apply(get(x, y)));
				}
			}
			return returned;
		} else {
			return new ApplyingFunctionPlayground(this, function);
		}
	}
	public default <Returned, Other> ImmutablePlayground<Returned> apply(ImmutablePlayground<Other> other,
			BiFunction<Content, Other, Returned> function, boolean eager) {
		if(eager) {
			Playground returned = new Playground<>(getWidth(), getHeight());
			for (int y = 0; y < getHeight(); y++) {
				for (int x = 0; x < getWidth(); x++) {
					returned.set(x, y, function.apply(get(x, y), other.get(x, y)));
				}
			}
			return returned;
		} else {
			return new ApplyingBiFunctionPlayground(this, other, function);
		}
	}
	public default ImmutablePlayground<Content> replace(DiscretePoint point, Content value) {
		return replace(p -> p.equals(point), p -> value);
	}
	public default ImmutablePlayground<Content> replace(Predicate<DiscretePoint> predicate, Function<DiscretePoint, Content> contentGenerator) {
		return new ProxyPlayground<Content>(this, predicate, contentGenerator);
	}
	public default <Type> Type accept(final PlaygroundVisitor<Type, Content> visitor) {
		visitor.startVisit(this);
		for (int y = 0; y < getHeight(); y++) {
			visitor.startVisitRow(y);
			for (int x = 0; x < getWidth(); x++) {
				visitor.visit(x, y, get(x, y));
			}
			visitor.endVisitRow(y);
		}
		return visitor.endVisit(this);
	}
	
	public default MutablePlayground<Content> readWriteProxy() {
		return new MutableProxy<Content>(this);
	}
}
