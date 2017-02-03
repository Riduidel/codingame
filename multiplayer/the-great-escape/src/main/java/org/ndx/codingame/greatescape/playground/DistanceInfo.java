package org.ndx.codingame.greatescape.playground;

import java.util.Comparator;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;

/**
 * Class providing both distance to exit and number of paths going through the point
 * @author ndelsaux
 *
 */
public class DistanceInfo extends DiscretePoint implements Comparable<DistanceInfo> {
	private int distance = Integer.MAX_VALUE;
	public DistanceInfo(final int x, final int y) {
		super(x, y);
	}

	public DistanceInfo(final DiscretePoint position) {
		super(position);
	}

	public void minimizeDistanceTo(final int i) {
		distance = Math.min(i, distance);
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public String toString() {
		return String.format("%d", distance);
	}

	@Override
	public int compareTo(final DistanceInfo o) {
		return Comparator.comparingInt(DistanceInfo::getDistance).compare(this, o);
	}
}
