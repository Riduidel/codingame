package org.ndx.codingame.lib2d;

import org.ndx.codingame.lib2d.base.AbstractPoint;

public class Triangle implements Polygon {
	
	public static Polygon from(AbstractPoint point1, AbstractPoint point2, AbstractPoint point3) {
		return new Triangle(point1, point2, point3);
	}

	private final AbstractPoint a, b, c;

	public Triangle(AbstractPoint a, AbstractPoint b, AbstractPoint c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}
}
