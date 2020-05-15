package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ndx.codingame.lib2d.MutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Type;

public interface SpringPlayfield extends MutablePlayground<Content> {

	public default DiscretePoint putBackOnPlayground(DiscretePoint point) {
		int x = point.x, y = point.y;
		if (x < 0) {
			x = getWidth() - 1;
		} else if (x >= getWidth()) {
			x = 0;
		}
		if (y < 0) {
			y = getHeight() - 1;
		} else if (y >= getHeight()) {
			y = 0;
		}
		return new DiscretePoint(x, y);
	}

	public default boolean allow(final DiscretePoint position, AbstractPac pac) {
		return allow(position.x, position.y, pac);
	}

	public default boolean allow(final int p_x, final int p_y, AbstractPac pac) {
		if (contains(p_x, p_y)) {
			return get(p_x, p_y).canBeWalkedOnBy(pac);
		}
		return false;
	}
	
	public Set<Pac> getAllPacs();

	public default List<Pac> getMyPacs() {
		List<Pac> returned = new ArrayList<>();
		for (Pac pac : getAllPacs()) {
			if (pac.mine)
				if(Type.DEAD!=pac.type)
					returned.add(pac);
		}
		return returned;
	}

	public Playground<Double> getZero();

	public Set<SmallPill> getSmallPills();

	public Set<BigPill> getBigPills();

	public Cache getCache();

	@Override
	default SpringPlayfield readWriteProxy() {
		return new VirtualPlayfield(this);
	}
}
