package org.ndx.codingame.lib2d.discrete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.MutablePlayground;
import org.ndx.codingame.lib2d.PlaygroundVisitor;

public class Playground<Content> implements MutablePlayground<Content> {
	private Object[][] content;
	public final int height;
	public final int width;
	private final Map<Class<?>, List<?>> entitiesByType = new HashMap<>();

	public Playground(final int width, final int height) {
		this(width, height, null);
	}
	public Playground(final int width, final int height, final Content defaultValue) {
		this.width = width;
		this.height = height;
		this.content = new Object[height][];
		for (int y = 0; y < height; y++) {
			this.content[y] = new Object[width];
			if(defaultValue!=null) {
				for (int x = 0; x < this.content[y].length; x++) {
					this.content[y][x] = defaultValue;
				}
			}
		}
	}

	public Playground(final ImmutablePlayground<Content> playground) {
		this(playground.getWidth(), playground.getHeight());
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.content[y][x]=playground.get(x, y);
			}
		}
	}

		
	public void clear() {
		clear(null);
	}
	public void clear(final Content defaultValue) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.content[y][x]=defaultValue;
			}
		}
	}

	@Override
	public void set(final DiscretePoint p, final Content c) {
		set(p.x, p.y, c);
	}

	@Override
	public void set(final int x, final int y, final Content c) {
		this.content[y][x] = c;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Content get(final DiscretePoint p) {
		return (Content) this.content[p.y][p.x];
	}

	@Override
	@SuppressWarnings("unchecked")
	public Content get(final int x, final int y) {
		return (Content) this.content[y][x];
	}

	@Override
	public boolean contains(final DiscretePoint point) {
		if(point.x<0 || point.x>=width) {
			return false;
		}
		if(point.y<0 || point.y>=height) {
			return false;
		}
		return true;
	}

	@Override
	public boolean contains(final int x, final int y) {
		if(x<0 || x>=width) {
			return false;
		}
		if(y<0 || y>=height) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder returned = new StringBuilder();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				returned.append("\t").append(this.content[y][x]).append(',');
			}
			returned.append('\n');
		}
		return returned.toString();
	}

	public ImmutablePlayground<Content> immutable() {
		return this;
	}

	public <Type> Type accept(final PlaygroundVisitor<Type, Content> visitor) {
		visitor.startVisit(this);
		for (int y = 0; y < height; y++) {
			visitor.startVisitRow(y);
			for (int x = 0; x < width; x++) {
				visitor.visit(x, y, get(x, y));
			}
			visitor.endVisitRow(y);
		}
		return visitor.endVisit(this);
	}
	
	public <Type extends Content> List<Type> getAll(final Class<Type> entityClass) {
		if(!entitiesByType.containsKey(entityClass)) {
			final List<Type> ofType = accept(new PlaygroundAdapter<List<Type>, Content>(new ArrayList<Type>()) {
				@Override
				public void visit(final int x, final int y, final Content content) {
					if(entityClass.isInstance(content)) {
						returned.add(entityClass.cast(content));
					}
				}
			});
			entitiesByType.put(entityClass, Collections.unmodifiableList(ofType));
		}
		return (List<Type>) entitiesByType.get(entityClass);
	}
	public boolean has(final DiscretePoint point) {
		return get(point)!=null;
	}
	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public int getHeight() {
		return height;
	}
}
