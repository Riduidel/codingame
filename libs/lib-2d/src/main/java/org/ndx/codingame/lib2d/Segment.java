package org.ndx.codingame.lib2d;

import java.util.Collection;
import java.util.stream.Collectors;

public class Segment extends Line {

	public Segment(Point first, Point second) {
		super(first, second);
	}
	
	public double length() {
		return first.distance2To(second);
	}

	public <Type extends Point> Type pointAtDistance(Point start, double distance, PointBuilder<Type> builder) {
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

	public boolean contains(Point point) {
		if(coeffs.matches(point)) {
			return 
					point.x<=Math.max(first.x, second.x) && point.x>=Math.min(first.x, second.x)
					&&
					point.y<=Math.max(first.y, second.y) && point.y>=Math.min(first.y, second.y);
		}
		return false;
	}
	
	@Override
	public Collection<Point> intersectionWith(Circle circle) {
		return super.intersectionWith(circle).stream().filter(this::contains).collect(Collectors.toSet());
	}
}