package org.ndx.codingame.lib2d.discrete;

public class Playground<Content> implements ImmutablePlayground<Content> {
	private Object[][] content;
	public final int height;
	public final int width;

	public Playground(int width, int height) {
		this(width, height, null);
	}
	public Playground(int width, int height, Content defaultValue) {
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

	public Playground(Playground<Content> playground) {
		this(playground.width, playground.height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.content[y][x]=playground.content[y][x];
			}
		}
	}

		
	public void clear() {
		clear(null);
	}
	public void clear(Content defaultValue) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.content[y][x]=defaultValue;
			}
		}
	}

	public void set(DiscretePoint p, Content c) {
		set(p.x, p.y, c);
	}

	public void set(int x, int y, Content c) {
		this.content[y][x] = c;
	}

	@SuppressWarnings("unchecked")
	public Content get(DiscretePoint p) {
		return (Content) this.content[p.y][p.x];
	}

	@SuppressWarnings("unchecked")
	public Content get(int x, int y) {
		return (Content) this.content[y][x];
	}

	public boolean contains(DiscretePoint point) {
		if(point.x<0 || point.x>=width)
			return false;
		if(point.y<0 || point.y>=height)
			return false;
		return true;
	}

	public boolean contains(int x, int y) {
		if(x<0 || x>=width)
			return false;
		if(y<0 || y>=height)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder returned = new StringBuilder();
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
}
