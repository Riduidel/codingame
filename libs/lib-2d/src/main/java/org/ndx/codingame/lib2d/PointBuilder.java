package org.ndx.codingame.lib2d;

import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public interface PointBuilder<Type extends AbstractPoint> {
	public static class DefaultPointBuilder implements PointBuilder<ContinuousPoint> {
		private DefaultPointBuilder() {
		}

		@Override
		public ContinuousPoint build(double x, double y) {
			return new ContinuousPoint(x, y);
		}
		
	}
	
	public static PointBuilder<ContinuousPoint> DEFAULT = new DefaultPointBuilder(); 

	Type build(double x, double y);
	
}