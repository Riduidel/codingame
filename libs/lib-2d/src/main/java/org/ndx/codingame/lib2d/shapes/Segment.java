package org.ndx.codingame.lib2d.shapes;

import java.util.Collection;
import java.util.stream.Collectors;

import org.ndx.codingame.lib2d.PointBuilder;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Segment extends Line {

	public static final double CENTER = 0.5;

	public Segment(final ContinuousPoint first, final ContinuousPoint second) {
		super(first, second);
	}
	
	public double length() {
		return first.distance2To(second);
	}

	public <Type extends AbstractPoint> Type pointAtDistance(final AbstractPoint start, final double distance, final PointBuilder<Type> builder) {
		return pointAtNTimesOf(start, distance/length(), builder);
	}

	@Override
	public String toString() {
		return "Segment [first=" + first + ", second=" + second
				+ ", length()=" + length() + "]";
	}

	@Override
	public Segment getDefiningSegment() {
		return this;
	}

	@Override
	public boolean contains(final ContinuousPoint point) {
		if(coeffs.matches(point)) {
			return 
					point.x<=Math.max(first.x, second.x) && point.x>=Math.min(first.x, second.x)
					&&
					point.y<=Math.max(first.y, second.y) && point.y>=Math.min(first.y, second.y);
		}
		return false;
	}
	
	@Override
	public Collection<ContinuousPoint> intersectionWith(final Circle circle) {
		return super.intersectionWith(circle).stream().filter(this::contains).collect(Collectors.toSet());
	}
	
	@Override
	public Collection<ContinuousPoint> intersectionWith(final Line line) {
		return super.intersectionWith(line).stream()
				.filter(p -> 
					// Intersection with line should be on this segment (that's to say between this segment extremitites 
					p.x>=Math.min(first.x, second.x) && p.x<=Math.max(first.x, second.x)
					&&
					p.y>=Math.min(first.y, second.y) && p.y<=Math.max(first.y, second.y)
				)
				.collect(Collectors.toList());
	}
	
	public Collection<ContinuousPoint> intersectionWith(final Segment segment) {
		return intersectionWith((Line) segment).stream()
				.filter(p -> 
				// Point is on other segment
				p.x>=Math.min(segment.first.x, segment.second.x) && p.x<=Math.max(segment.first.x, segment.second.x)
				&&
				p.y>=Math.min(segment.first.y, segment.second.y) && p.y<=Math.max(segment.first.y, segment.second.y)
			)
			.collect(Collectors.toList());
	}
	
	public boolean intersectsWith(final Segment segment) {
		return !intersectionWith(segment).isEmpty();
	}
	
	public Segment orthogonalSegment(final ContinuousPoint point) {
		final ContinuousPoint projected = project(point);
		return new Segment(point, projected);
	}
	
	@Override
	public double distance2To(final AbstractPoint point) {
		if (point instanceof ContinuousPoint) {
			final ContinuousPoint continuous = (ContinuousPoint) point;
			final Segment orthogonalSegment = orthogonalSegment(continuous);
			if(contains(orthogonalSegment.second)) {
				return Math.min(
						orthogonalSegment.length(),
						Math.min(first.distance2To(point), second.distance2To(continuous))
						);
			} else {
				return Math.min(first.distance2To(point), second.distance2To(continuous));
			}
		} else {
			throw new UnsupportedOperationException("Pas de calcul de distance entre un segment et un AbstractPoint");
		}
	}

	public Line toLine() {
		return new Line(first, second);
	}

	public <Type extends ContinuousPoint> Type pointAtDistance(final double d, final PointBuilder<Type> builder) {
		return pointAtDistance(first, d, builder);
	}
	
	@Override
	protected Line create(final ContinuousPoint targetPosition, final ContinuousPoint secondTarget) {
		return new Segment(targetPosition, secondTarget);
	}
}