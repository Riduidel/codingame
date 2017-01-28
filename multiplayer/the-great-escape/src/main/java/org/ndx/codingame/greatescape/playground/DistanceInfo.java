package org.ndx.codingame.greatescape.playground;

import org.ndx.codingame.lib2d.discrete.Playground;

public class DistanceInfo extends Playground<Integer> {
	public static final int TOO_FAR = 1000;

	public DistanceInfo(final int width, final int height) {
		super(width, height, TOO_FAR);
	}
}
