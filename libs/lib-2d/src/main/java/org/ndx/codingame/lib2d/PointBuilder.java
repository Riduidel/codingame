package org.ndx.codingame.lib2d;

public interface PointBuilder<Type extends Point> {
	public static class DefaultPointBuilder implements PointBuilder<Point> {
		private DefaultPointBuilder() {
		}

		@Override
		public Point build(double x, double y) {
			return new Point(x, y);
		}
		
	}
	
	public static PointBuilder<Point> DEFAULT = new DefaultPointBuilder(); 

	Type build(double x, double y);
	
}