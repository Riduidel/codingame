package org.ndx.codingame.greatescape.computers;

import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;

public class DistancePlayground extends Playground<DistanceInfo> {

	public DistancePlayground(final Playfield tested) {
		super(tested.width, tested.height);
		for(int x = 0; x<tested.width; x++) {
			for (int y = 0; y < tested.height; y++) {
				set(x, y, new DistanceInfo());
			}
		}
	}

	public void setDistance(final DiscretePoint point, final int distance) {
		get(point).setDistance(distance);
	}

	public int getDistance(final DiscretePoint point) {
		return get(point).getDistance();
	}
	@Override
	public String toString() {
		final StringBuilder returned = new StringBuilder();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				returned.append("\t").append(get(x, y)).append(',');
			}
			returned.append('\n');
			for (int x = 0; x < width; x++) {
				returned.append("\t").append(String.format(DistanceInfo.FORMAT, get(x, y).getReverseDistance())).append(',');
			}
			returned.append('\n');
		}
		return returned.toString();
	}

}
