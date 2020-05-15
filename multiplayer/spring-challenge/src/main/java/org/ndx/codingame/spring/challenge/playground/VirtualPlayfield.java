package org.ndx.codingame.spring.challenge.playground;

import java.util.HashSet;
import java.util.Set;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.MutableProxy;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.SmallPill;

public class VirtualPlayfield extends MutableProxy<Content> implements SpringPlayfield {

	private Set<SmallPill> smallPills;
	private Set<BigPill> bigPills;
	private Set allPacs;

	public VirtualPlayfield(SpringPlayfield playfield) {
		super(playfield);
		this.bigPills = new HashSet<>(playfield.getBigPills());
		this.smallPills = new HashSet<>(playfield.getSmallPills());
		this.allPacs = new HashSet<>(playfield.getAllPacs());
	}
	
	@Override
	public VirtualPlayfield getSource() {
		return (VirtualPlayfield) super.getSource();
	}

	@Override
	public Set<Pac> getAllPacs() {
		return allPacs;
	}

	@Override
	public Playground<Double> getZero() {
		return getSource().getZero();
	}

	@Override
	public Set<SmallPill> getSmallPills() {
		return smallPills;
	}

	@Override
	public Set<BigPill> getBigPills() {
		return bigPills;
	}

	@Override
	public Cache getCache() {
		return getSource().getCache();
	}

	@Override
	public void set(DiscretePoint p, Content c) {
		Content previous = get(p);
		super.set(p, c);
		setSpecificContent(p, previous, c);
	}

	@Override
	public void set(int x, int y, Content c) {
		Content previous = get(x, y);
		super.set(x, y, c);
		setSpecificContent(new DiscretePoint(x, y), previous, c);
	}

	public void setSpecificContent(DiscretePoint location, Content previous, Content next) {
		next.accept(new SpecificContentSetter(this, previous, location, next));
	}

}
