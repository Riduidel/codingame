package org.ndx.codingame.greatescape.playground;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;

public class DistanceInfoPlayground extends Playground<DistanceInfo> {
	public DistanceInfoPlayground(final int width, final int height) {
		super(width, height);
	}

	public DistanceInfo getOrCreate(final DiscretePoint position) {
		DistanceInfo returned = get(position);
		if(returned==null) {
			set(position, returned = new DistanceInfo(position));
		}
		return returned;
	}
}
