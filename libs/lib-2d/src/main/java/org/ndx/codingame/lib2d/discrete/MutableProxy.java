package org.ndx.codingame.lib2d.discrete;

import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.MutablePlayground;

public class MutableProxy<Content> extends Playground<Content> implements MutablePlayground<Content> {

	private final ImmutablePlayground<Content> source;

	public MutableProxy(ImmutablePlayground<Content> immutablePlayground) {
		super(immutablePlayground.getWidth(), immutablePlayground.getHeight(), (Content) null);
		this.source = immutablePlayground;
	}

	@Override
	public Content get(DiscretePoint p) {
		Content returned = super.get(p);
		return returned==null ? source.get(p) : returned;
	}
	
	@Override
	public Content get(int x, int y) {
		Content returned = super.get(x, y);
		return returned==null ? source.get(x, y) : returned;
	}
	
	@Override
	public boolean contains(DiscretePoint point) {
		boolean returned = super.contains(point);
		return  returned ? returned : source.contains(point);
	}
	
	@Override
	public boolean contains(int x, int y) {
		boolean returned = super.contains(x, y);
		return  returned ? returned : source.contains(x, y);
	}

	public ImmutablePlayground<Content> getSource() {
		return source;
	}
}
