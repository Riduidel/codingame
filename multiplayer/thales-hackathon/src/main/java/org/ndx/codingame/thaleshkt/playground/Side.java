package org.ndx.codingame.thaleshkt.playground;

import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.thaleshkt.Constants;

public enum Side {
	LEFT(Constants.LEFT_EDGE),
	RIGHT(Constants.RIGHT_EDGE);
	
	public final Segment edge;
	
	private Side(Segment edge) {
		this.edge = edge;
	}
}
