package org.ndx.codingame.lib2d.continuous.shapes.bezier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.ndx.codingame.lib2d.PointBuilder;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Line;
import org.ndx.codingame.lib2d.shapes.PolySegment;
import org.ndx.codingame.lib2d.shapes.Segment;

public class PolynomialBezierCurve {

	private static final int DISTANCE_DIVIDER = 50;
	private final List<ContinuousPoint> all;
	public final ContinuousPoint to;
	public final ContinuousPoint from;
	private PolySegment polySegment;
	private final double distanceDivider;

	public PolynomialBezierCurve(final ContinuousPoint from, final List<ContinuousPoint> control, final ContinuousPoint to) {
		this(from, control, to, DISTANCE_DIVIDER);
	}
	public PolynomialBezierCurve(final ContinuousPoint from, final List<ContinuousPoint> control, final ContinuousPoint to, final double distanceDivider) {
		this.from = from;
		this.to = to;
		all = new ArrayList<>(control.size()+2);
		all.add(from);
		all.addAll(control);
		all.add(to);
		this.distanceDivider = distanceDivider;
	}

	PolynomialBezierCurve(final List<ContinuousPoint> list, final double distanceDivider) {
		from = list.get(0);
		to = list.get(list.size()-1);
		all = list;
		this.distanceDivider = distanceDivider;
	}

	public <Type extends ContinuousPoint> Type pointAtDistance(final double distance, final PointBuilder<Type> builder) {
		return toPolySegment().pointAtDistance(distance, builder);
	}


	/**
	 * Generates a polysegment from this bezier curve. For that, recursive cut is applied to generate polysegment fragments
	 * @return
	 */
	public PolySegment toPolySegment() {
		if(polySegment==null) {
			polySegment = createPolySegment();
		}
		return polySegment;
	}

	private PolySegment createPolySegment() {
		if(allAreQuiteAligned()) {
			return new PolySegment(Arrays.asList(new Segment(from, to)));
		} else {
			// Split that bezier curve in two equal parts
			final Deque<Deque<Segment>> construction = createConstructionAt(Segment.CENTER);
			final ContinuousPoint center = construction.getLast().getFirst().pointAtNTimes(Segment.CENTER);
			// Create both children bezier curves
			final List<ContinuousPoint> first = new ArrayList<>();
			final List<ContinuousPoint> second = new ArrayList<>();
			for(final Deque<Segment> level : construction) {
				first.add(level.getFirst().first);
				second.add(level.getLast().second);
			}
			first.add(center);
			// Now second is built in bad order, reverse it to have valid content
			second.add(center);
			Collections.reverse(second);
			final PolynomialBezierCurve firstCurve = new PolynomialBezierCurve(first, distanceDivider);
			final PolynomialBezierCurve secondCurve = new PolynomialBezierCurve(second, distanceDivider);
			// Assemble their polysegments
			return firstCurve.toPolySegment().extendWith(secondCurve.toPolySegment());
		}
	}

	private boolean allAreQuiteAligned() {
		final Line segment = new Line(from, to);
		final double range = from.distance2To(to)/distanceDivider;
		for (final ContinuousPoint continuousPoint : all) {
			if(continuousPoint!=from && continuousPoint!=to) {
				if(segment.distance2To(continuousPoint)>range) {
					return false;
				}
			}
		}
		return true;
	}

	private Deque<Deque<Segment>> createConstructionAt(final double center2) {
		Deque<Segment> segments = toSegments(all);
		final Deque<Deque<Segment>> returned = new LinkedList<>();
		returned.add(segments);
		while(segments.size()>1) {
			segments = findBaryecentersAt(segments, center2);
			returned.add(segments);
		}
		return returned;
	}

	private Deque<Segment> findBaryecentersAt(final Deque<Segment> segments, final double center2) {
		final Deque<Segment> returned = new LinkedList<>();
		ContinuousPoint first = null;
		for(final Segment s : segments) {
			final ContinuousPoint barycenter = s.pointAtNTimes(center2);
			if(first!=null) {
				returned.add(new Segment(first, barycenter));
			}
			first = barycenter;
		}
		return returned;
	}

	private Deque<Segment> toSegments(final List<ContinuousPoint> points) {
		final Deque<Segment> segments = new LinkedList<>();
		ContinuousPoint first = null;
		for(final ContinuousPoint p : points) {
			if(first!=null) {
				segments.add(new Segment(first, p));
			}
			first = p;
		}
		return segments;
	}

	public double length() {
		return toPolySegment().length();
	}

	public ContinuousPoint pointAtDistance(final double distance) {
		return pointAtDistance(distance, from);
	}
}
