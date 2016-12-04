package org.ndx.codingame.lib2d;

import org.ndx.codingame.lib2d.base.AbstractPoint;

public interface PointBuilder<Type extends AbstractPoint> {
	Type build(double x, double y);
	
}