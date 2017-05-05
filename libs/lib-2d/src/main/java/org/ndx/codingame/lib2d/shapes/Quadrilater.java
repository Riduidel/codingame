package org.ndx.codingame.lib2d.shapes;

import org.ndx.codingame.lib2d.base.AbstractPoint;

public class Quadrilater implements Polygon {
	private final AbstractPoint a, b, c, d;

	public Quadrilater(final AbstractPoint a, final AbstractPoint b, final AbstractPoint c, final AbstractPoint d) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

//	public static Polygon from(Point point, Point point2, Point point3, Point point4) {
//		boolean sameLengths = 
//		return null;
//	}
}
