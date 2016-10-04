package org.ndx.codingame.lib2d;

public class Segment extends Line {

	public Segment(Point first, Point second) {
		super(first, second);
	}
	
	public double length() {
		return first.distance2To(second);
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
			if(coeffs.isVerticalLine()) {
				return -1*Math.signum(point.x-first.x)==Math.signum(point.x-second.x) || point.x==first.x || point.x == second.y;
			} else {
				return -1*Math.signum(point.y-first.y)==Math.signum(point.y-second.y) || point.y==first.y || point.y == second.y;
			}
		}
		return false;
	}
}