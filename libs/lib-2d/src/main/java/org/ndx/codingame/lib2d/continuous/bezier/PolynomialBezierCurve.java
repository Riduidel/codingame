package org.ndx.codingame.lib2d.continuous.bezier;

import java.util.List;

import org.ndx.codingame.lib2d.PointBuilder;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.PolySegment;

public class PolynomialBezierCurve implements BezierCurve {

	private final ContinuousPoint from;
	private final ContinuousPoint to;
	private final List<ContinuousPoint> control;

	public PolynomialBezierCurve(final ContinuousPoint from, final List<ContinuousPoint> control, final ContinuousPoint to) {
		this.from = from;
		this.to = to;
		this.control = control;
	}

	public <Type extends ContinuousPoint> Type pointAtDistance(final double distance, final PointBuilder<Type> builder) {
		// implement that shit !
	}


	/**
	 * Generates a polysegment from this bezier curve. For that, recursive cut is applied to generate polysegment fragments
	 * @return
	 */
	public PolySegment toPolySegment() {
		
	}
}
