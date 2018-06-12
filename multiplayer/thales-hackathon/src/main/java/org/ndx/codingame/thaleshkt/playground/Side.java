package org.ndx.codingame.thaleshkt.playground;

import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.thaleshkt.Constants;
import org.ndx.codingame.thaleshkt.MetaParameters;

public enum Side {
	LEFT(Constants.LEFT_EDGE, Geometry.at(MetaParameters.FLAG_OFFSET, 0.0)),
	RIGHT(Constants.RIGHT_EDGE, Geometry.at(-1*MetaParameters.FLAG_OFFSET, 0.0));
	
	public final Segment edge;
	public final ContinuousPoint asDefender;
	
	private Side(Segment edge, ContinuousPoint l) {
		this.edge = edge;
		this.asDefender = l;
	}
}
