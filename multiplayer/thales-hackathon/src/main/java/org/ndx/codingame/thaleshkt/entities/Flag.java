package org.ndx.codingame.thaleshkt.entities;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Circle;

public class Flag implements ConstructableInUnitTest {

	public final Circle position;

	public Flag(int flagX, int flagY) {
		this.position = new Circle(new ContinuousPoint(flagX, flagY), 150);
	}

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		return new StringBuilder(multilinePrefix)
				.append("new Flag(")
				.append((int) position.center.x).append(", ")
				.append((int) position.center.y)
				.append(");");
	}

}
