package org.ndx.codingame.lib2d.continuous.bezier;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class QuadraticBezierCurve implements BezierCurve {

	private ContinuousPoint from;
	private ContinuousPoint control;
	private ContinuousPoint to;

	public QuadraticBezierCurve(ContinuousPoint from, ContinuousPoint continuousPoint, ContinuousPoint to) {
		this.from = from;
		this.control = continuousPoint;
		this.to = to;
	}

}
