package org.ndx.codingame.lib2d;

import java.util.Collection;
import java.util.stream.Collectors;

import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Segment extends Line {

	public Segment(ContinuousPoint first, ContinuousPoint second) {
		super(first, second);
	}
	
	public double length() {
		return first.distance2To(second);
	}

	public <Type extends ContinuousPoint> Type pointAtDistance(ContinuousPoint start, double distance, PointBuilder<Type> builder) {
		return pointAtNTimesOf(start, distance/length(), builder);
	}

	@Override
	public String toString() {
		return "Segment [first=" + first + ", second=" + second
				+ ", length()=" + length() + "]";
	}

	public Segment getDefiningSegment() {
		return this;
	}

	public boolean contains(ContinuousPoint point) {
		if(coeffs.matches(point)) {
			return 
					point.x<=Math.max(first.x, second.x) && point.x>=Math.min(first.x, second.x)
					&&
					point.y<=Math.max(first.y, second.y) && point.y>=Math.min(first.y, second.y);
		}
		return false;
	}
	
	@Override
	public Collection<ContinuousPoint> intersectionWith(Circle circle) {
		return super.intersectionWith(circle).stream().filter(this::contains).collect(Collectors.toSet());
	}
	
	@Override
	public Collection<ContinuousPoint> intersectionWith(Line line) {
		return super.intersectionWith(line).stream()
				.filter(p -> 
					p.x>=Math.min(first.x, second.x) && p.x<=Math.max(first.x, second.x)
					&&
					p.y>=Math.min(first.y, second.y) && p.y<=Math.max(first.y, second.y)
				)
				.collect(Collectors.toList());
	}
	
	public Collection<ContinuousPoint> intersectionWith(final Segment segment) {
		return intersectionWith((Line) segment).stream()
				.filter(p -> 
				p.x>=Math.min(segment.first.x, segment.second.x) && p.x<=Math.max(segment.first.x, segment.second.x)
				&&
				p.y>=Math.min(segment.first.y, segment.second.y) && p.y<=Math.max(segment.first.y, segment.second.y)
			)
			.collect(Collectors.toList());
	}
	
	public boolean intersectsWith(Segment segment) {
		return !intersectionWith(segment).isEmpty();
	}
}