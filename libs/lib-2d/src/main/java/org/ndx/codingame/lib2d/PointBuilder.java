package org.ndx.codingame.lib2d;

public interface PointBuilder<Type extends Point> {
	public static class DefaultPointBuilder implements PointBuilder<Point> {

		@Override
		public Point build(double x, double y) {
			return new Point(x, y);
		}
		
	}

	Type build(double x, double y);
	
}