package org.ndx.codingame.lib2d.continuous.bezier;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class CubicBezierCurve implements BezierCurve {

	private ContinuousPoint from;
	private ContinuousPoint to;
	private ContinuousPoint control1;
	private ContinuousPoint control2;

	public CubicBezierCurve(ContinuousPoint from, ContinuousPoint continuousPoint, ContinuousPoint continuousPoint2,
			ContinuousPoint to) {
		this.from = from;
		this.control1 = continuousPoint;
		this.control2 = continuousPoint2;
		this.to = to;
	}

}
