package org.ndx.codingame.thaleshkt;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;

public class Constants {

	public static final int WIDTH = 10_000;
	public static final int HEIGHT = 8_000;
	public static final Segment RIGHT_EDGE = new Segment(new ContinuousPoint(WIDTH, 0), new ContinuousPoint(WIDTH, HEIGHT));
	public static final Segment LEFT_EDGE = new Segment(new ContinuousPoint(0, 0), new ContinuousPoint(0, HEIGHT));
	public static final int UFO_RADIUS = 400;
	public static final int FLAG_RADIUS = 150;


}
