package org.ndx.codingame.the_accountant;

import java.util.ArrayList;
import java.util.Collection;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class DataPoint extends ContinuousPoint {
	public final int id;

	public DataPoint(int id, double x, double y) {
		super(x, y);
		this.id = id;
	}

	@Override
	public String toString() {
		return "DataPoint [id=" + id + ", x=" + x + ", y=" + y + "]";
	}
}
