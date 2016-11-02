package org.ndx.codingame.lib2d;

import org.ndx.codingame.lib2d.base.AbstractPoint;

public class Quadrilater implements Polygon {
	private final AbstractPoint a, b, c, d;

	public Quadrilater(AbstractPoint a, AbstractPoint b, AbstractPoint c, AbstractPoint d) {
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
