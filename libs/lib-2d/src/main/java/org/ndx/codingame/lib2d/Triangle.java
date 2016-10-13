package org.ndx.codingame.lib2d;

public class Triangle implements Polygon {
	
	public static Polygon from(Point point1, Point point2, Point point3) {
		return new Triangle(point1, point2, point3);
	}

	private final Point a, b, c;

	public Triangle(Point a, Point b, Point c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}
}
