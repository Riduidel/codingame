package org.ndx.codingame.greatescape.computers;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class DistanceInfo {
	public static final String FORMAT = "%5d";
	private static final int UPPER_BOUND = 10000;
	private int distance = UPPER_BOUND;
	private int reverseDistance = UPPER_BOUND; 
	private int paths = 0;

	public void setDistance(final int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public void addPath() {
		paths++;
	}

	public int getPaths() {
		return paths;
	}

	@Override
	public String toString() {
		return String.format(FORMAT, distance);
	}

	public int getReverseDistance() {
		return reverseDistance;
	}

	public void setReverseDistance(final int reverseDistance) {
		this.reverseDistance = reverseDistance;
	}

	public void addPathFrom(final DistancePlayground returned, final DiscretePoint point) {
		addPath();
		final DistanceInfo info = returned.get(point);
		reverseDistance = Math.min(reverseDistance, info.reverseDistance+1);
	}
}
